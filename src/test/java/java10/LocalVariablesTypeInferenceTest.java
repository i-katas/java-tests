package java10;

import org.junit.Test;

import java.util.function.IntBinaryOperator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author i-katas
 * @since 1.0
 */
public class LocalVariablesTypeInferenceTest {
    @Test
    public void localVariables() {
        var value = "foo";

        assertThat(value, equalTo("foo"));
    }

    @Test
    public void lambdaParameters() {
        IntBinaryOperator sum = (var a, var b) -> a + b;

        assertThat(sum.applyAsInt(1, 2), equalTo(3));
    }
}
