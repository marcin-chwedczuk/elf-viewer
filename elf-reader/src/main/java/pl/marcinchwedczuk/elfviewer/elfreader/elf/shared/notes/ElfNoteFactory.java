package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfSectionHeader;

import static java.util.Objects.requireNonNull;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.ElfNoteTypeGnu.GNU_ABI_TAG;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.ElfNoteTypeGnu.GNU_BUILD_ID;

public class ElfNoteFactory<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        > {
    private final NativeWord<NATIVE_WORD> nativeWord;
    private final ElfSectionHeader<NATIVE_WORD> sectionHeader;

    public ElfNoteFactory(NativeWord<NATIVE_WORD> nativeWord,
                          ElfSectionHeader<NATIVE_WORD> sectionHeader) {
        this.nativeWord = requireNonNull(nativeWord);
        this.sectionHeader = requireNonNull(sectionHeader);
    }

    public ElfNote mkNote(int nameLength,
                          byte[] nameBytes,
                          String name,
                          int descriptorLength,
                          byte[] descriptor,
                          int type) {
        if (sectionHeader.hasName(ElfSectionNames.GNU_BUILD_ATTRIBUTES)
                && name.startsWith("GA")) {
            return new ElfNoteGnuBuildAttribute(
                    nameLength, nameBytes, name,
                    descriptorLength, descriptor,
                    ElfNoteTypeGnu.fromValue(type));
        } else if ("GNU".equals(name)) {
            return createGnuNote(
                    nameLength, name,
                    descriptorLength, descriptor,
                    type);
        } else {
            return new ElfNote(
                    nameLength, name,
                    descriptorLength, descriptor,
                    type);
        }
    }

    public static ElfNoteGnu createGnuNote(int nameLength,
                                           String name,
                                           int descriptorLength,
                                           byte[] descriptor,
                                           int type) {
        if (!"GNU".equals(name)) {
            throw new IllegalArgumentException(
                    "Invalid GNU note name, expecting 'GNU' but got '" + name + "'.");
        }

        ElfNoteTypeGnu gnuType = ElfNoteTypeGnu.fromValue(type);

        if (gnuType.is(GNU_ABI_TAG)) {
            return new ElfNoteGnuABITag(nameLength, name, descriptorLength, descriptor, gnuType);
        }

        if (gnuType.is(GNU_BUILD_ID)) {
            return new ElfNoteGnuBuildId(nameLength, name, descriptorLength, descriptor, gnuType);
        }

        return new ElfNoteGnu(nameLength, name, descriptorLength, descriptor, gnuType);
    }
}
