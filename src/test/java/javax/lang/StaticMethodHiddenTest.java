package javax.lang;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author xiaoliang
 * @since 2019-08-24
 */
@SuppressWarnings("AccessStaticViaInstance")
public class StaticMethodHiddenTest {

    @Test
    public void hidesSuperclassStaticMethodWithStaticTypeOfSubclass() {
        Subclass it = new Subclass();

        assertThat(it.name(), equalTo("self"));
        assertThat(it.name(), equalTo(Subclass.name()));
    }

    @Test
    public void invokesSuperclassStaticMethodWithStaticTypeOfSuperclass() {
        Superclass it = new Subclass();

        assertThat(it.name(), equalTo("super"));
        assertThat(it.name(), equalTo(Superclass.name()));
    }

    static class Superclass {
        /**
         * You can add the {@code final} modifier to avoid the static method of superclass to be hidden in subclass.
         */
        //@formatter:off
        /*final*/ static String name() {
            return "super";
        }
        //@formatter:on
    }

    static class Subclass extends Superclass {
        static String name() {
            return "self";
        }
    }
}
