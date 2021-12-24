package pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes;

import java.util.Objects;

import static pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.notes.ElfGnuBuildAttributeType.*;

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

        ElfGnuBuildAttributeType attributeType = ElfGnuBuildAttributeType.fromValue(nameBytes[3]);
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
     * @return This can return either Long, String or Boolean
     * depending on the attribute.
     */
    public Object buildAttributeValue() {
        // Name format GA<attr-type><attr-name><attr-value>
        if (nameBytes.length < 5)
            return null;

        ElfGnuBuildAttributeType attributeType = ElfGnuBuildAttributeType.fromValue(nameBytes[3]);

        int valueStart = -1;
        if (attributeType.isKnownValue()) {
            valueStart = 4;
        } else {
            // Value starts after name, remember to skip \x00 byte
            for (int curr = 3; curr < nameBytes.length; curr++) {
                valueStart = curr + 1;
                if (nameBytes[curr] == 0) break;
            }
        }

        switch ((char)nameBytes[2]) {
            case '+': return Boolean.TRUE;
            case '!': return Boolean.FALSE;

            case '$': {
                StringBuilder value = new StringBuilder();
                for (int curr = valueStart; curr < nameBytes.length; curr++) {
                    value.append((char)nameBytes[curr]);
                }
                return value.toString();
            }

            case '*': {
                // Numeric value in little endian
                long value = 0;
                for (int curr = valueStart, shift = 0; curr < nameBytes.length; curr++, shift += 8) {
                    value |= ((nameBytes[curr] & 0xffL) << shift);
                }

                // Special cases taken from:
                // https://github.com/bminor/binutils-gdb/blob/master/binutils/readelf.c#L21049
                if (attributeType.is(POSITION_INDEPENDENCE)) {
                    switch ((int)value) {
                        case 0: return "static";
                        case 1: return "pic";
                        case 2: return "PIC";
                        case 3: return "pie";
                        case 4: return "PIE";
                    }
                } else if (attributeType.is(STACK_PROTECTOR)) {
                    switch ((int)value) {
                        /* Based upon the SPCT_FLAG_xxx enum values in gcc/cfgexpand.c.  */
                        case 0: return "off";
                        case 1: return "on";
                        case 2: return "all";
                        case 3: return "strong";
                        case 4: return "explicit";
                    }
                }

                return (Long)value;
            }

            default:
                // Unknown value type
                return null;
        }

    }
}
