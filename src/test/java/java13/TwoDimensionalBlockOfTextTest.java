package java13;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author i-katas
 * @since 1.0
 */
public class TwoDimensionalBlockOfTextTest {
    @Test
    public void terminated() {
        String text = """
                      line
                      """;

        assertThat(text, equalTo("line\n"));
    }

    @Test
    public void unterminated() {
        String text = """
                      line""";

        assertThat(text, equalTo("line"));
    }
}
