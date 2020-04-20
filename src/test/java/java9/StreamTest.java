package java9;

import org.junit.Test;

import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author i-katas
 * @since 1.0
 */
public class StreamTest {
    @Test
    public void iterateOnCondition() {
        IntPredicate lessThan3 = n -> n < 3;

        IntStream numbers = IntStream.iterate(0, lessThan3, n -> n + 1);

        assertThat(numbers.toArray(), equalTo(new int[]{0, 1, 2}));
    }
}
