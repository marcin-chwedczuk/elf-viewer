package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ByteListTest {
    @Test
    void smoke_test() {
        ByteList bl = new ByteList();
        bl.add((byte) 1);
        bl.add((byte) 2);
        bl.add((byte) 3);
        byte[] b = bl.toByteArray();
        assertThat(b)
                .isEqualTo(new byte[] { 1, 2, 3 });

        for (int i = 3; i < 100; i++) {
            bl.add((byte)i);
        }
        b = bl.toByteArray();

        assertThat(b[0]).isEqualTo((byte)1);
        assertThat(b[99]).isEqualTo((byte)99);
    }

    @Test
    void toAsciiString_works() {
        ByteList buffer = new ByteList(0);
        buffer.add((byte)'a');
        buffer.add((byte)'b');
        buffer.add((byte)'c');

        String result = buffer.toAsciiString();
        assertThat(result)
                .isEqualTo("abc");
    }
}