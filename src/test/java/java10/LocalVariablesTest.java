package java10;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author i-katas
 * @since 1.0
 */
public class LocalVariablesTest {
    @Test
    public void defineLocalVariables() {
        var value = "foo";

        assertThat(value, equalTo("foo"));
    }
}
