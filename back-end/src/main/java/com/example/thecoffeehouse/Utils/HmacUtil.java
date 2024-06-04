package com.example.thecoffeehouse.Utils;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class HmacUtil {

    public static String hmacSHA256(final String key, final String data) {
        return hmac(key, data, "HmacSHA256");
    }

    public static String hmacSHA512(final String key, final String data) {
        return hmac(key, data, "HmacSHA512");
    }

    private static String hmac(final String key, final String data, final String algorithm) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac = Mac.getInstance(algorithm);
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, algorithm);
            hmac.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }
}


