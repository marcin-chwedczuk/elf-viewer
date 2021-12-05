package pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments;

import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32ProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32BasicSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class Elf32SegmentFactory {
    public Elf32Segment wrap(ElfSegment<Integer> segment) {
        return new Elf32Segment(segment);
    }
}
