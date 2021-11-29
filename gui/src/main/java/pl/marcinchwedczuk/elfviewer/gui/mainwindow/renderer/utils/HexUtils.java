package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.utils;

public class HexUtils {
    private static final String[] HEX_CODES = new String[256];
    static {
        for (int i = 0; i <= 0xff; i++) {
            HEX_CODES[i] = String.format("%02X", i);
        }
    }

    public static String toHexNoPrefix(byte b) {
       return HEX_CODES[b & 0xff];
    }
}
