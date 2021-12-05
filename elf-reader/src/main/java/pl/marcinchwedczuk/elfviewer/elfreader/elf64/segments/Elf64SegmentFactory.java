package pl.marcinchwedczuk.elfviewer.elfreader.elf64.segments;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegment;

public class Elf64SegmentFactory {
    public Elf64Segment wrap(ElfSegment<Long> segment) {
        return new Elf64Segment(segment);
    }
}
