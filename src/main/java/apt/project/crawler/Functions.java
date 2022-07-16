package apt.project.crawler;


import org.apache.commons.validator.routines.UrlValidator;

import java.net.URL;
import java.net.URLConnection;

public class Functions {
    private Functions() {}

    /**
     * Normalizes urls, that is, removes the following:
     *  - Leading protocols, and any following www, or m.
     *  - Trailing queries.
     * @param url the url to be normalized.
     * @return    the normalized url.
     */
    public static String NormalizeURL(String url) {
        int [][] possibleStarts = {
                {url.lastIndexOf("http://www."), 11},
                {url.lastIndexOf("https://www."), 12},
                {url.lastIndexOf("http://m."), 9},
                {url.lastIndexOf("https://m."), 10},
                {url.lastIndexOf("http://"), 7},
                {url.lastIndexOf("https://"), 8},
                {url.lastIndexOf("m."), 2},
                {url.lastIndexOf("www."), 4},
        };

        for (int[] possibleStart : possibleStarts) {
            if (!(possibleStart[0] == -1)) {
                url = url.substring(possibleStart[1]);
                break;
            }
        }

        // Removing the query part...
        int queryIdx = url.indexOf('?');
        if (queryIdx != -1)
            url = url.substring(0, queryIdx);

        // Removing the # part
        queryIdx = url.indexOf('#');
        if (queryIdx != -1)
            url = url.substring(0, queryIdx);

        // Removing the trailing /
        if (url.charAt(url.length() - 1) == '/')
            url = url.substring(0, url.length() - 1);

        return url;
    }

    public static String ExtractDomainFromUrl(String url) {

        int idx = url.indexOf('/');
        if (idx != -1) {
            url = url.substring(0, idx);
        }
        return url;
    }

    public static boolean IsURLValid(String url) {
        UrlValidator v = new UrlValidator();
        return v.isValid(url);
    }

    public static boolean DoesURLHaveProtocol(String url) {

        if (url.startsWith("http"))
            return true;
        else return url.startsWith("ftp");

    }

    public static boolean IsConnectedToInternet() {
        try {
            URL url = new URL("https://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
