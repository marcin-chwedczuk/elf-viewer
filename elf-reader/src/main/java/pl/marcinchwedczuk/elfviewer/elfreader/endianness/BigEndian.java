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

    @Override
    public long toUnsignedLong(byte[] bytes) {
        return ((0xffL & bytes[0]) << 56)
                | ((0xffL & bytes[1]) << 48)
                | ((0xffL & bytes[2]) << 40)
                | ((0xffL & bytes[3]) << 32)
                | ((0xffL & bytes[4]) << 24)
                | ((0xffL & bytes[5]) << 16)
                | ((0xffL & bytes[6]) << 8)
                | ((0xffL & bytes[7]) << 0);
    }
}
