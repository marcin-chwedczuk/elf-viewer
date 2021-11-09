package pl.marcinchwedczuk.elfviewer.elfreader;

public class Util {
    private Util() { }

    public static String quote(String s) {
        return String.format("'%s'", s);
    }
}
