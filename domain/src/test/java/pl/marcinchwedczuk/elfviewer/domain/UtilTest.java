package pl.marcinchwedczuk.elfviewer.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UtilTest {
    @Test
    void quote_works() {
        assertThat(Util.quote("foo"))
                .isEqualTo("'foo'");
    }
}