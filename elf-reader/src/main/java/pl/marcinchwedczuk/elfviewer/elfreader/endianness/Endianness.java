package pl.marcinchwedczuk.elfviewer.elfreader.endianness;

public interface Endianness {
    short toUnsignedShort(byte[] bytes);
}
