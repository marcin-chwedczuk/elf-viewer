package pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32DynamicTag;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32DynamicTags;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.elfreader.io.StructuredFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.Args;

import java.util.List;

import static pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames.INTERP;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.DYNAMIC;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.PROGBITS;

public class Elf32DynamicSection extends Elf32Section {
    public Elf32DynamicSection(Elf32File elfFile,
                               Elf32SectionHeader header) {
        super(elfFile, header);
        Args.checkSectionType(header, DYNAMIC);
   }

   public List<Elf32DynamicTag> dynamicTags() {
        Elf32DynamicTags dynamicTags = new Elf32DynamicTags(
                elfFile(),
                header());

        return dynamicTags.getTags();
   }

    @Override
    public void accept(Elf32Visitor visitor) {
        visitor.enter(this);
        visitor.exit(this);
    }
}
