package java13;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author i-katas
 * @since 1.0
 */
public class SwitchExpressionTest {
    @Test
    public void assignment() {
        int code = 1;

        String result = switch (code) {
            case 0 -> "foo";
            case 1 -> "bar";
            default -> throw new IllegalArgumentException("Invalid code: " + code);
        };

        assertThat(result, equalTo("bar"));
    }
}
