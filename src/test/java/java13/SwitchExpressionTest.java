package java13;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author i-katas
 * @since 1.0
 */
public class SwitchExpressionTest {
    @Test
    public void labeledStatements() {
        String code = "foo";

        String result = switch (code) {
            case "foo" -> "bar";
            default -> throw new IllegalArgumentException("Invalid code: " + code);
        };

        assertThat(result, equalTo("bar"));
    }

    @Test
    public void yieldStatements() {
        String code = "foo";

        String result = switch (code) {
            case "foo" -> {
                yield "bar";
            }
            default -> throw new IllegalArgumentException("Invalid code: " + code);
        };

        assertThat(result, equalTo("bar"));
    }
}
