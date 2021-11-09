package pl.marcinchwedczuk.elfviewer.elfreader.endianness;

public class LittleEndian implements Endianness {
    @Override
    public short toUnsignedShort(byte[] bytes) {
        return (short)(0xffff & (bytes[0] | bytes[1] << 8));
    }
}
