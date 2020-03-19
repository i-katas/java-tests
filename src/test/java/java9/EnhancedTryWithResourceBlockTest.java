package java9;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author i-katas
 * @since 1.0
 */
public class EnhancedTryWithResourceBlockTest {
    @Test
    public void useEffectiveFinalVariableInTryWithResourceBlockWithoutDefineAnyVariables() throws IOException {
        Writer content = new StringWriter();
        //content = new StringWriter();

        try (content) {
            content.write("foo");

            assertThat(content.toString(), equalTo("foo"));
        }
    }
}
