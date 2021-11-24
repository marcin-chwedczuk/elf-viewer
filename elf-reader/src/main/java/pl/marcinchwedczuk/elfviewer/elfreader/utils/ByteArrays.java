package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import java.math.BigInteger;

// TODO: Move to non exported class
public class ByteArrays {
    private ByteArrays() { }

    public static String toHexString(byte[] bytes) {
        return new BigInteger(1, bytes).toString(16);
    }

    public static String toHexString(byte[] bytes, String separator) {
        if (bytes.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(
                // XX<separator>XX<separator>XX
                bytes.length * 2 + (bytes.length - 1) * separator.length()
        );

        for (int i = 0; i < bytes.length; i++) {
            if (i != 0) {
                sb.append(separator);
            }

            String hex = Integer.toHexString(0xFF & bytes[i]).toUpperCase();
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }

        return sb.toString();
    }
}
