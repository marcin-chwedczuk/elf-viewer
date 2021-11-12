package pl.marcinchwedczuk.elfviewer.elfreader.elf32;

import pl.marcinchwedczuk.elfviewer.elfreader.meta.ElfApi;

@ElfApi("Elf32_Nhdr")
public class Elf32NoteInformation {
    @ElfApi("namesz")
    private int nameLength;

    @ElfApi("name")
    private String name;

    @ElfApi("descsz")
    private int descriptionLength;

    @ElfApi("desc")
    private String description;

    @ElfApi("type")
    private int type;

    public Elf32NoteInformation(int nameLength,
                                String name,
                                int descriptionLength,
                                String description,
                                int type) {
        this.nameLength = nameLength;
        this.name = name;
        this.descriptionLength = descriptionLength;
        this.description = description;
        this.type = type;
    }

    @Override
    public String toString() {
       return String.format("%4d %s %4d %s 0x%08x",
               nameLength, name, descriptionLength, description, type);
    }
}
