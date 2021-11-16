package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import org.junit.jupiter.api.Test;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.IntPartialEnum;

import java.util.*;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.*;

public class PartialEnumTest {
    @Test
    void equals_works_for_enum_values() {
        // known values
        assertThat(NumberEnum.ONE)
                .isEqualTo(NumberEnum.ONE);

        assertThat(NumberEnum.ONE)
                .isNotEqualTo(NumberEnum.TWO);

        // unknown values
        assertThat(NumberEnum.fromValue(100))
                .isEqualTo(NumberEnum.fromValue(100));

        assertThat(NumberEnum.fromValue(100))
                .isNotEqualTo(NumberEnum.fromValue(101));
    }

    @Test
    void equals_works_for_values_from_diff_enums() {
        NumberEnum n = NumberEnum.fromValue(123);
        LetterEnum l = LetterEnum.fromValue(123);

        assertThat(n).isNotEqualTo((Object) l);
        assertThat(l).isNotEqualTo((Object) n);
    }

    @Test
    public void byValue_works_for_known_value() {
        NumberEnum n1 = NumberEnum.ONE;
        NumberEnum n2 = NumberEnum.fromValue(1);

        assertThat(n2.value())
                .isEqualTo(1);

        assertThat(n2.name())
                .isEqualTo("ONE");

        assertThat(n1)
                .isEqualTo(n2);
    }

    @Test
    public void byValue_works_for_unknown_value() {
        NumberEnum n1 = NumberEnum.fromValue(123);
        NumberEnum n2 = NumberEnum.fromValue(123);

        assertThat(n2.value())
                .isEqualTo(123);

        assertThat(n2.name())
                .isNull();

        assertThat(n1)
                .isEqualTo(n2);
    }

    @Test
    public void testing_known_unknown_value_works() {
        NumberEnum known = NumberEnum.ONE;
        NumberEnum unknown = NumberEnum.fromValue(123);

        assertThat(known.isKnownValue())
                .isTrue();
        assertThat(known.isUnknownValue())
                .isFalse();

        assertThat(unknown.isKnownValue())
                .isFalse();
        assertThat(unknown.isUnknownValue())
                .isTrue();
    }

    @Test
    void byName_works_for_known_value() {
        NumberEnum n2 = NumberEnum.fromName("ONE");

        assertThat(n2.value())
                .isEqualTo(1);

        assertThat(n2.name())
                .isEqualTo("ONE");

        assertThat(n2)
                .isEqualTo(NumberEnum.ONE);
    }

    @Test
    void byName_throws_exception_for_unknown_value() {
        assertThatThrownBy(() -> NumberEnum.fromName("blah"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void toString_works() {
        assertThat(NumberEnum.ONE.toString())
                .isEqualTo("ONE");

        assertThat(NumberEnum.fromValue(0xabcd).toString())
                .isEqualTo("0x0000abcd");
    }

    @Test
    void is_works() {
        assertThat(NumberEnum.ONE.is(NumberEnum.ONE))
                .isTrue();

        assertThat(NumberEnum.ONE.is(NumberEnum.THREE))
                .isFalse();
    }

    @Test
    void isOneOf_works() {
        assertThat(NumberEnum.ONE.isOneOf(NumberEnum.ONE, NumberEnum.TWO))
                .isTrue();

        assertThat(NumberEnum.ONE.isOneOf(NumberEnum.TWO, NumberEnum.THREE))
                .isFalse();
    }

    @Test
    void knownValues_returns_list_of_known_values() {
        assertThat(NumberEnum.knownValues().toArray())
                .isEqualTo(List.of(NumberEnum.ONE, NumberEnum.TWO, NumberEnum.THREE).toArray());
    }


    public static class NumberEnum extends IntPartialEnum<NumberEnum> {
        private static final Map<Integer, NumberEnum> byValue = mkByValueMap();
        private static final Map<String, NumberEnum> byName = mkByNameMap();

        public static final NumberEnum ONE = new NumberEnum(1, "ONE");
        public static final NumberEnum TWO = new NumberEnum(2, "TWO");
        public static final NumberEnum THREE = new NumberEnum(3, "THREE");

        private NumberEnum(int value) {
            super(value);
        }

        private NumberEnum(int value, String name) {
            super(value, name, byValue, byName);
        }

        public static NumberEnum fromValue(int value) {
            return IntPartialEnum.fromValueOrCreate(value, byValue, NumberEnum::new);
        }

        public static NumberEnum fromName(String name) {
            return IntPartialEnum.fromName(name, byName);
        }

        public static Collection<NumberEnum> knownValues() {
            return IntPartialEnum.knownValues(byValue);
        }
    }

    public static class LetterEnum extends IntPartialEnum<LetterEnum> {
        private static final Map<Integer, LetterEnum> byValue = mkByValueMap();
        private static final Map<String, LetterEnum> byName = mkByNameMap();

        public static final LetterEnum A = new LetterEnum(1, "A");
        public static final LetterEnum B = new LetterEnum(2, "B");
        public static final LetterEnum C = new LetterEnum(3, "C");

        private LetterEnum(int value) {
            super(value);
        }

        private LetterEnum(int value, String name) {
            super(value, name, byValue, byName);
        }

        public static LetterEnum fromValue(int value) {
            return IntPartialEnum.fromValueOrCreate(value, byValue, LetterEnum::new);
        }

        public static LetterEnum fromName(String name) {
            return IntPartialEnum.fromName(name, byName);
        }

        public static Collection<LetterEnum> knownValues() {
            return IntPartialEnum.knownValues(byValue);
        }
    }
}
