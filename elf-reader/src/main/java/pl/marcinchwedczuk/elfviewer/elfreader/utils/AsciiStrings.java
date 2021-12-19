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

    public static String toCSourceCodeString(byte[] bytes) {
        // +32 is a heuristic to avoid extra allocations when there are some
        // escape sequences in the input string.
        StringBuilder sb = new StringBuilder(bytes.length + 32);

        for (int i = 0; i < bytes.length; i++) {
            // ignore last 00 byte
            if (bytes[i] == 0x00 && i == (bytes.length-1))
                continue;

            byte b = bytes[i];
            switch ((char)b) {
                case (char)0x07: sb.append("\\a"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                case (char)0x0b: sb.append("\\v"); break;
                case '\\': sb.append("\\\\"); break;
                default:
                    if (isPrintableCharacter(b)) {
                        sb.append((char)b);
                    } else {
                        sb.append("\\x")
                                .append(String.format("%02x", 0xff & b));
                    }
            }
        }

        return sb.toString();
    }
}
