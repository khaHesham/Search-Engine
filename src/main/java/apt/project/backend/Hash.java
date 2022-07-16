package apt.project.backend;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Hash {

    private Hash() {}

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b: bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


    public static String hashUserAndSearchPhrase(String IP, String phrase) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest((IP + phrase).getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


}
