package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes;

import java.util.Objects;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.ElfGnuBuildAttribute.*;

public class ElfNoteGnuBuildAttribute extends ElfNoteGnu {
    private final byte[] nameBytes;

    public ElfNoteGnuBuildAttribute(int nameLength,
                                    byte[] nameBytes,
                                    String name,
                                    int descriptorLength,
                                    byte[] descriptor,
                                    ElfNoteTypeGnu type) {
        super(nameLength, name, descriptorLength, descriptor, type);

        if (name == null || !name.startsWith("GA"))
            throw new IllegalArgumentException("Invalid note name, GNU build attributes must start with 'GA'.");

        this.nameBytes = Objects.requireNonNull(nameBytes);
    }

    public String buildAttributeName() {
        // Name format GA<attr-type><attr-name>
        if (nameBytes.length < 4)
            return null;

        ElfGnuBuildAttribute attributeType = ElfGnuBuildAttribute.fromValue(nameBytes[3]);
        if (attributeType.is(SPECIFICATION_VERSION)) {
            return "version";
        } else if (attributeType.is(STACK_PROTECTOR)) {
            return "stack protector";
        } else if (attributeType.is(RELRO)) {
            return "relro";
        } else if (attributeType.is(STACK_SIZE)) {
            return "stack size";
        } else if (attributeType.is(BUILD_TOOL_VERSION)) {
            return "build tool version";
        } else if (attributeType.is(ABI)) {
            return "ABI";
        } else if (attributeType.is(POSITION_INDEPENDENCE)) {
            return "code position independence";
        } else if (attributeType.is(SHORT_ENUMS)) {
            return "short enums";
        } else {
            int byteValue = nameBytes[3] & 0xff;
            if (byteValue < 32) {
                // Unknown attribute - reserved range
                return null;
            } if (byteValue >= 127) {
                // Unknown attribute - reserved range
                return null;
            } else {
                // String
                StringBuilder name = new StringBuilder();
                for (int curr = 3; curr < nameBytes.length; curr++) {
                    if (nameBytes[curr] == 0) break;
                    name.append((char)nameBytes[curr]);
                }
                return name.toString();
            }
        }
    }

    /**
     * @return This can return either NATIVE_WORD, String or Boolean
     * depending on the attribute.
     */
    public Object buildAttributeValue() {
        return null;
    }
}
