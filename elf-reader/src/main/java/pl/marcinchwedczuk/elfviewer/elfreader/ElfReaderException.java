package pl.marcinchwedczuk.elfviewer.elfreader;

public class ElfReaderException extends RuntimeException {
    public ElfReaderException(String message) {
        super(message);
    }

    public ElfReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
