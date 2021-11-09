package pl.marcinchwedczuk.elfviewer.elfreader.endianness;

public class BigEndian implements Endianness {
    @Override
    public short toUnsignedShort(byte[] bytes) {
        return (short)(0xffff & (bytes[0] << 8 | bytes[1]));
    }
}
