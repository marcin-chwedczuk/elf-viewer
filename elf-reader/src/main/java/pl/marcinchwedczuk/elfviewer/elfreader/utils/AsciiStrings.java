package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import java.nio.charset.StandardCharsets;

public class AsciiStrings {
    private AsciiStrings() {}

    public static String toAsciiString(byte[] bytes) {
        return new String(
                bytes,
                0,
                bytes.length,
                StandardCharsets.US_ASCII);
    }

    public static String toPrintableAsciiString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length);

        for (int i = 0; i < bytes.length; i++) {
            if (isPrintableCharacter(bytes[i])) {
                sb.append((char)bytes[i]);
            } else {
                sb.append('.');
            }
        }

        return sb.toString();
    }

    public static boolean isPrintableCharacter(byte b) {
        int ub = b & 0xff;
        return (ub >= 0x20 && ub < 0x7f);
    }
}
