package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class Args {
    private Args() { }

    public static void checkSectionType(Elf32SectionHeader section, ElfSectionType... allowedTypes) {
        if (section.type().isOneOf(allowedTypes)) {
            return;
        }

        String message;
        if (allowedTypes.length == 1) {
            message = String.format("Expected section to be of type %s but was of type %s.",
                    allowedTypes[0], section.type());
        } else {
            String allowedTypesString = Arrays.stream(allowedTypes)
                    .map(Objects::toString)
                    .collect(joining(","));

            message = String.format("Expected section to be one of types [%s] but was %s.",
                    allowedTypesString, section.type());
        }

        throw new IllegalArgumentException(message);
    }
}
