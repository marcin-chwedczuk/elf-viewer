package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.dto;

public class SymbolTableEntryDto {
    public final String symbolIndex;

    private final String nameIndex;
    private final String name;

    private final String value;
    private final String size;

    private final String info;
    private final String binding;
    private final String symbolType;

    private final String other;
    private final String visibility;

    private final String index;
    private final String relatedSectionName;

    public SymbolTableEntryDto(String symbolIndex,
                               String nameIndex,
                               String name,
                               String value,
                               String size,
                               String info,
                               String binding,
                               String symbolType,
                               String other,
                               String visibility,
                               String index,
                               String relatedSectionName) {
        this.symbolIndex = symbolIndex;
        this.nameIndex = nameIndex;
        this.name = name;
        this.value = value;
        this.size = size;
        this.info = info;
        this.binding = binding;
        this.symbolType = symbolType;
        this.other = other;
        this.visibility = visibility;
        this.index = index;
        this.relatedSectionName = relatedSectionName;
    }

    public String getOffset() {
        return symbolIndex;
    }

    public String getNameIndex() {
        return nameIndex;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getSize() {
        return size;
    }

    public String getInfo() {
        return info;
    }

    public String getBinding() {
        return binding;
    }

    public String getSymbolType() {
        return symbolType;
    }

    public String getOther() {
        return other;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getIndex() {
        return index;
    }

    public String getRelatedSectionName() {
        return relatedSectionName;
    }
}
