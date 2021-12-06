package pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegment;

public class Elf32SegmentFactory {
    public Elf32Segment wrap(ElfSegment<Integer> segment) {
        return new Elf32Segment(segment);
    }
}
