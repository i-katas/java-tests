package java13;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author i-katas
 * @since 1.0
 */
public class StringBlockTest {
    @Test
    public void verticalBar() {
        String text = """
                      first
                      last
                      """;

        assertThat(text, equalTo("first\nlast\n"));
    }
}
