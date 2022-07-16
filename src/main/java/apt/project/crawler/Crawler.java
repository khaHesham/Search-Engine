package apt.project.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLException;
import java.io.*;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.BitSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static apt.project.crawler.Functions.*;


/** Used when executing the process that parses robots.txt
 *  Is thrown when the output unexpectedly doesn't contain either
 *  ALLOWED, or DISALLOWED.
 */
class RobotsException extends Exception {
    RobotsException() {
        super();
    }
    RobotsException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class Crawler extends Thread {

    private static final Path pagesPath = Paths.get(System.getProperty("user.dir")).resolve("pages");
    private static final Path robotsPath  = pagesPath.resolve(".robots");
    private static final Pattern subURIPattern = Pattern.compile("^(\\/[a-zA-Z0-9._$%&*!?~()-]+(\\/)?)+$");
    private static final Pattern subMediaPattern = Pattern.compile(".*(.png)|(.mp4)|(.mp3)|(.jpg)$");
    public boolean terminated = false;

    private String currentUrl;
    private long myPosition;


    private final Frontier frontier;
    private final FingerPrints fingerPrints;
    private final CrawledUrls crawledUrls;
    private final SimHash simHash;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final int MAX_DOC_COUNT = 3000;

    public Crawler(Frontier frontier, FingerPrints fingerPrints, CrawledUrls crawledUrls) {
        this.frontier = frontier;
        this.fingerPrints = fingerPrints;
        this.crawledUrls = crawledUrls;

        simHash = new SimHash();
    }

    /**
     * Sets the terminated flag to true.
     * This flag gets checked every iteration of the main loop in run().
     * If the value is set, then the function returns, and the thread terminates.
     * This function is only called by the main thread when a SIGINT is caught.
     * The main thread, is by default, waiting to join on these threads.
     */
    public void Terminate() {
        terminated = true;
    }

    /**
     * Checks if the url is disallowed.
     * It utilizes google's robots.txt parser, through the jar in the lib directory.
     * @param   url                 the url to check for.
     * @return  true                if it's allowed
     *          false               if it's not allowed
     * @throws  RobotsException     if any error occurs.
     * @see <a href= "https://github.com/google/robotstxt-java">Google's robots.txt parser</a>
     **/
    private boolean CheckIfAllowed(String url) throws RobotsException {

        String domain = ExtractDomainFromUrl(url);
        Path thisRobotsPath = robotsPath.resolve(String.valueOf(myPosition));

        try {
            HttpResponse<Path> response = SendRequest(String.format("%s/robots.txt", domain), thisRobotsPath);
            if (response == null || response.statusCode() > 400) {
                System.err.printf("here is robots: %s/robots.txt\n", domain);
                throw new RobotsException();
            }

            Process proc = Runtime.getRuntime().exec(new String []{"java", "-jar", "lib/google-robots.jar", "--agent", "CmpBot", "--url",
                    String.format("https://%s", url), "--file", thisRobotsPath.toString()});

            BufferedReader inStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            StringBuilder builder = new StringBuilder(); String str;

            while((str = inStream.readLine()) != null) {
                builder.append(str).append("\n");
            }

            Files.deleteIfExists(thisRobotsPath);
            str = builder.toString();

            if (str.contains("DISALLOWED")) {
                return false;
            } else if (str.contains("ALLOWED")) {
                return true;
            } else {
                System.err.printf("here is robots again: %s/robots.txt\n", domain);
                System.err.println(str);
                System.err.println(thisRobotsPath.toString());
                throw new RobotsException();
            }
        } catch (RobotsException e) {
            throw e;
        } catch (Exception e) {
            throw new RobotsException("Caught exception while reading the file", e);
        }
    }

    private HttpRequest FormRequest(String url) {
        try {
            return HttpRequest.newBuilder().uri(new URI(url)).GET().timeout(Duration.of(10, ChronoUnit.SECONDS)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpResponse<Path> FormResponse(HttpRequest request, Path p, boolean secure) {
        try {
            return HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NEVER)
                    .build()
                    .send(request,
                            HttpResponse.BodyHandlers.ofFile(p)
                    );
        } catch(HttpTimeoutException e) {
            DeletePageFile(p);
            return null;
        } catch (SSLException e) {

            // If the request we failed to send was secure, try a non-secure one.
            // If it's not, and an SSLException was still thrown (don't know how that might happen),
            //      then return null and stop trying.
            if (secure) {
                HttpRequest nonSecure = FormRequest("http://" + currentUrl);
                if (nonSecure == null) return null;
                else {
                    return FormResponse(nonSecure, p, false);
                }
            } else {
                DeletePageFile(p);
                return null;
            }
        } catch (ConnectException e) {

            if (IsConnectedToInternet()) {
                DeletePageFile(p);
            } else {
                // Return it back to the frontier. Ask Kirollos.
                System.err.println("No internet connection.. Suspending work..");
                terminated = true;
            }
            return null;
        } catch (IOException e) {
            // Return it back to the frontier. Ask Kirollos.
            System.out.printf(ANSI_YELLOW + "here ioexception is null %s" + ANSI_RESET, request.uri());
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            return FormResponse(request, p, secure);
        }
    }

    /**
     * Fetches a page at a certain url, stores it in the pages' path.
     * The name of the file stored is solely the index of the url in the database (no extensions).
     * It first tries making a secure request, then a non-secure one if it fails with an SSLException.
     *
     * If a ConnectionException is encountered, it checks for internet connection.
     *  - If there is, it deletes the file and drops the url from th db.
     *  - If there is not, it sets the terminated flag to true, and terminates (in the next iteration of the run() function)
     *
     * @param url   the url to fetch. (normalized urls, having no protocol or queries)
     * @param p     the path to store the HTML page. (/pages/id)
     * @return      the response object.
     * */
    private HttpResponse<Path> SendRequest(String url, Path p) {

        HttpRequest request = FormRequest("https://" + url);
        if (request == null)
            return null;

        return FormResponse(request, p, true);
    }

    private void DeletePageFile(Path p) {
        try {
            Files.deleteIfExists(p);
        } catch (IOException e) {
            System.err.printf("error deleting an html page at location: %s\n", p);
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        myPosition = Thread.currentThread().getId();

        outerLoop: while (crawledUrls.size() < MAX_DOC_COUNT) {

            if (terminated) {
                System.err.printf("%s exiting...\n", Thread.currentThread().getName());
                return;
            }

            //????????????????????????????????????????
            do {
                currentUrl = frontier.FetchUrl();
                if (currentUrl.isEmpty() && terminated)
                    return;
            } while (currentUrl.isEmpty());

            Matcher m = subMediaPattern.matcher(currentUrl.toLowerCase());
            if (m.find())
                continue outerLoop;

            System.out.println(Thread.currentThread().getName() + ", fetching: " + currentUrl);

            Path newPagePath = pagesPath.resolve(Paths.get(String.valueOf(myPosition)));

            // Checking if the page is allowed for us to crawl...
            /*
            try {
                if (!CheckIfAllowed(currentUrl)) {
                    System.out.printf(ANSI_YELLOW + "%s: not allowed to crawl page: %s.. skipping..\n" + ANSI_RESET, Thread.currentThread().getName(), currentUrl);
                    continue;
                }
            } catch (RobotsException e) {
                e.printStackTrace();
                continue;
            }
            */
            // Sending the initial request to the site.
            HttpResponse<Path> response = SendRequest(currentUrl, newPagePath);
            if (response == null) { continue; }

            /*
             * Keep requesting while it's a redirect, then set the two variable strings: normalizedURLBeforeRedirect
             * and normalizedURLAfterRedirect, for comparison later.
             */
            String normalizedURLBeforeRedirect, normalizedURLAfterRedirect;
            {
                String tempURL = currentUrl;

                String location;

                while (response.statusCode() == 301 || response.statusCode() == 308) {

                    if (response.headers().firstValue("location").isEmpty()) {
                        DeletePageFile(newPagePath);
                        continue outerLoop;
                    }

                    location = response.headers().firstValue("location").get();
                    Matcher matcher = subURIPattern.matcher(location);

                    if (matcher.find())
                        tempURL += location;
                    else
                        tempURL = location;

                    System.out.printf("It's a redirect: %s\n", tempURL);
                    response = SendRequest(tempURL, newPagePath);

                    if (response == null) { continue outerLoop; }
                }

                normalizedURLBeforeRedirect = currentUrl;
                normalizedURLAfterRedirect = NormalizeURL(response.uri().toString());
            }

            /* If the page is not found, or any error has occurred, drop and delete the url. */
            if (response.statusCode() > 400) {
                System.out.printf(ANSI_YELLOW + "error fetching page. status code: %d.. deleting file and dropping from db\n" + ANSI_RESET, response.statusCode());
                DeletePageFile(newPagePath);
                continue;
            }

            /*
             * Reading the contents of the page and creating the elements for scraping it later.
             * Also, checking the language of the page in the HTML tag <html lang=""></html>
             * If lang is not explicitly "en", then we discard the page.
             * */
            if (response.headers().firstValue("Content-Type").isPresent()) {
                if (!response.headers().firstValue("Content-Type").get().contains("text/html")) {
                    System.out.printf(ANSI_YELLOW + "page's content-type is not text/html. content-type: %s\n" + ANSI_RESET,
                            response.headers().allValues("Content-Type"));
                    DeletePageFile(newPagePath);
                    continue ;
                }
            } else {
                System.err.printf(ANSI_YELLOW + "page's content-type header is not present. page url = %s. aborting...\n" + ANSI_RESET, currentUrl);
                DeletePageFile(newPagePath);
                continue ;
            }

            /*
             * If the url is different after redirecting (not just difference in protocol, but in domain or routes),
             * then check if the one we are directed to already exists in the database.
             * A) If it exists, do the following:
             *    Delete the page file and continue to the next iteration.
             * B) If it doesn't: then continue as usual.
             * */
            {
                if (!normalizedURLAfterRedirect.equals(normalizedURLBeforeRedirect)) {
                    System.out.printf("Before: %s .. After: %s\n", normalizedURLBeforeRedirect, normalizedURLAfterRedirect);
                    if (frontier.HasUrl(normalizedURLAfterRedirect)){
                        System.out.printf("%s already exists as %s...\n", currentUrl, normalizedURLAfterRedirect);
                        DeletePageFile(newPagePath);
                        continue;
                    }
                }
            }

            Elements links;
            {
                StringBuilder builder = new StringBuilder();
                BufferedReader buf;
                try {
                    buf = new BufferedReader(new FileReader(newPagePath.toString()));
                } catch (FileNotFoundException e) {
                    System.err.println("error: page doesn't exist.");
                    continue;
                }

                String str;
                try {
                    while ((str = buf.readLine()) != null) {
                        builder.append(str).append("\n");
                    }
                } catch (IOException e) {
                    System.err.println("error reading the page's content");
                    DeletePageFile(newPagePath);
                    continue;
                }

                Document doc = Jsoup.parse(builder.toString());
                try {
                    buf.close();
                } catch (IOException e) {
                    System.err.println("error closing the buffer.");
                    e.printStackTrace();
                }
                links = doc.getElementsByTag("a");

                // We'll only scrape this page if it's in english, otherwise we discard it.
                Elements html = doc.getElementsByTag("html");
                for (Element x : html) {
                    String lang = x.attributes().get("lang");
                    if (!lang.startsWith("en")) {
                        DeletePageFile(newPagePath);
                        continue outerLoop;
                    }
                    break;
                }

                String []bodyText = doc.text().split(" ");
                BitSet fingerPrint = simHash.hash(bodyText, doc.charset());

                // If the fingerprint of this page already exists, continue to the next iteration.
                if (fingerPrints.contains(fingerPrint))
                    continue;

                fingerPrints.add(fingerPrint);
                System.out.println("Number URLS = " + crawledUrls.size());
                crawledUrls.put(currentUrl, fingerPrint);
            }

            String newUrl;
            for (Element link : links) {

                newUrl = link.attributes().get("href");

                /*
                 * Sometimes, the links are in the form: /example
                 * That is, they are not complete urls.
                 * I've encountered this case scraping the facebook.com page for example.
                 * Then, the url should be transformed to: facebook.com/example
                 * While "facebook.com" being the url we are at, at the moment.
                 * */

                if (!IsURLValid(newUrl)) {

                    final Matcher matcher = subURIPattern.matcher(newUrl);
                    if (matcher.find()) {
                        newUrl = currentUrl + newUrl;
                        if (!IsURLValid(newUrl))
                            continue;
                    } else if (!DoesURLHaveProtocol(newUrl)) {
                        newUrl = "https://" + newUrl;
                        if (!IsURLValid(newUrl))
                            continue;
                    } else {
                        continue;
                    }
                }

                newUrl = NormalizeURL(newUrl);
                frontier.InsertUrl(currentUrl, newUrl);
            }

            System.out.println(Thread.currentThread().getName() + ", Calculating PageRank and updating frontier\n");
            frontier.Update();

            DeletePageFile(newPagePath);
        }
    }

}