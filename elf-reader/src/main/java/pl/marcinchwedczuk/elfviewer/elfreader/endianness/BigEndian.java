package pl.marcinchwedczuk.elfviewer.elfreader.endianness;

public class BigEndian implements Endianness {
    @Override
    public short toUnsignedShort(byte[] bytes) {
        return (short)(
                (0xff & bytes[0]) << 8
                | (0xff & bytes[1])
        );
    }

    @Override
    public int toUnsignedInt(byte[] bytes) {
        return (0xff & bytes[0]) << 24
                | (0xff & bytes[1]) << 16
                | (0xff & bytes[2]) << 8
                | (0xff & bytes[3]);
    }
}
