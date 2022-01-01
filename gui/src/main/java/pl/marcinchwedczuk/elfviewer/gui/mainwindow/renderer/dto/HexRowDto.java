package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto;

import pl.marcinchwedczuk.elfviewer.elfreader.io.FileView;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.AsciiStrings;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.utils.HexUtils;

import static java.util.Objects.requireNonNull;

public class HexRowDto {
    private final String address;

    private final long rowOffset;
    private final FileView data;

    private String hexViewCache = null;
    private String asciiStringCache = null;

    public HexRowDto(
            String address,
            long rowOffset,
            FileView data)
    {
        this.address = address;
        this.rowOffset = rowOffset;
        this.data = requireNonNull(data);
    }

    public String getAddress() { return address; }

    private String b(int byteOffset) {
        long offset = rowOffset + byteOffset;
        if (0 <= offset && offset < data.viewLength()) {
            return HexUtils.toHexNoPrefix(data.read(offset));
        }

        return "";
    }

    public String b0() { return b(0x0); }
    public String b1() { return b(0x1); }
    public String b2() { return b(0x2); }
    public String b3() { return b(0x3); }
    public String b4() { return b(0x4); }
    public String b5() { return b(0x5); }
    public String b6() { return b(0x6); }
    public String b7() { return b(0x7); }
    public String b8() { return b(0x8); }
    public String b9() { return b(0x9); }
    public String ba() { return b(0xa); }
    public String bb() { return b(0xb); }
    public String bc() { return b(0xc); }
    public String bd() { return b(0xd); }
    public String be() { return b(0xe); }
    public String bf() { return b(0xf); }

    public String printableAsciiView() {
        if (asciiStringCache != null)
            return asciiStringCache;

        StringBuilder sb = new StringBuilder(16);

        for (long i = rowOffset + 0x00; i <= rowOffset + 0x0f; i++) {
            if (0 <= i && i < data.viewLength()) {
                byte b = data.read(i);
                if (AsciiStrings.isPrintableCharacter(b)) {
                    sb.append((char)b);
                } else {
                    sb.append('.');
                }
            } else {
                sb.append(' ');
            }
        }

        asciiStringCache = sb.toString();
        return asciiStringCache;
    }

    public String hexView() {
        if (hexViewCache != null)
            return hexViewCache;

        StringBuilder sb = new StringBuilder(16);

        for (int i = 0x00; i <= 0x0f; i++) {
            sb.append(b(i));
            // do not insert spaces to allow for continuous search like 'af013f'
        }

        hexViewCache = sb.toString();
        return hexViewCache;
    }
}
