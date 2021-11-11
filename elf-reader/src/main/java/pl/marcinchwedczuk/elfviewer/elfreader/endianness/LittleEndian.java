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
}
