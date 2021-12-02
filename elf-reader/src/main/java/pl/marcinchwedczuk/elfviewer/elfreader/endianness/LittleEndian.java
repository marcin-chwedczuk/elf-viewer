package pl.marcinchwedczuk.elfviewer.elfreader.endianness;

public class LittleEndian implements Endianness {
    @Override
    public short toUnsignedShort(byte[] bytes) {
        return (short)(
                (0xff & bytes[0])
                | ((0xff & bytes[1]) << 8)
        );
    }

    @Override
    public int toUnsignedInt(byte[] bytes) {
        return (0xff & bytes[0])
                | ((0xff & bytes[1]) << 8)
                | ((0xff & bytes[2]) << 16)
                | ((0xff & bytes[3]) << 24);
    }

    @Override
    public long toUnsignedLong(byte[] bytes) {
        return (0xffL & bytes[0])
                | ((0xffL & bytes[1]) << 8)
                | ((0xffL & bytes[2]) << 16)
                | ((0xffL & bytes[3]) << 24)
                | ((0xffL & bytes[4]) << 32)
                | ((0xffL & bytes[5]) << 40)
                | ((0xffL & bytes[6]) << 48)
                | ((0xffL & bytes[7]) << 56);
    }
}
