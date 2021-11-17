package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

public class IntFlagsTest {
    @Test
    public void creating_from_value_works() {
        // r w x
        UnixPermissions perm = new UnixPermissions(4 + 1);

        assertThat(perm.hasFlag(UnixPermissions.R))
                .isTrue();
        assertThat(perm.hasFlag(UnixPermissions.W))
                .isFalse();

        assertThat(perm.toString())
                .isEqualTo("R|X");
    }

    @Test
    void equals_works() {
        UnixPermissions perm1 = new UnixPermissions(4 + 1);
        UnixPermissions perm2 = new UnixPermissions()
                .setFlag(UnixPermissions.R)
                .setFlag(UnixPermissions.X);

        assertThat(perm1)
                .isEqualTo(perm2);

        assertThat(perm2)
                .isEqualTo(perm1);

        UnixPermissions perm3 = new UnixPermissions()
                .setFlag(UnixPermissions.W)
                .setFlag(UnixPermissions.X);

        assertThat(perm3)
                .isNotEqualTo(perm1);

        assertThat(perm1)
                .isNotEqualTo(perm3);
    }

    @Test
    public void setting_and_clearing_flags_works() {
        UnixPermissions perm = new UnixPermissions();
        assertThat(perm.hasFlag(UnixPermissions.X))
                .isFalse();
        assertThat(perm.toString())
                .isEqualTo("");

        perm = perm.setFlag(UnixPermissions.X);
        assertThat(perm.hasFlag(UnixPermissions.X))
                .isTrue();
        assertThat(perm.toString())
                .isEqualTo("X");

        perm = perm.clearFlag(UnixPermissions.X);
        assertThat(perm.hasFlag(UnixPermissions.X))
                .isFalse();
        assertThat(perm.toString())
                .isEqualTo("");
    }

    // TODO: Add tests for other use cases

    public static class UnixPermissions extends IntFlags<UnixPermissions> {
        public static final Flag<UnixPermissions> R = flag("R", 1 << 2);
        public static final Flag<UnixPermissions> W = flag("W", 1 << 1);
        public static final Flag<UnixPermissions> X = flag("X", 1 << 0);

        public UnixPermissions(int init) {
            super(init);
        }

        public UnixPermissions(Flag<UnixPermissions>... flags) {
            super(flags);
        }

        @Override
        protected UnixPermissions mkCopy(int newRaw) {
            return new UnixPermissions(newRaw);
        }

        @Override
        public Collection<Flag<UnixPermissions>> flags() {
            return List.of(R, W, X);
        }
    }
}
