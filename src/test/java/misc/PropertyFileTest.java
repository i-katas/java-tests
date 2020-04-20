package misc;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import static java.lang.ClassLoader.getSystemResource;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author selonj
 * @since 2019-08-20
 */
public class PropertyFileTest {
    @Test
    public void loadPropertyFileAsBinaryStreamDoesNotSupportsUnicodeChars() throws IOException {
        Properties it = new Properties();
        try (InputStream in = resourceAsStream("unicode-chars.properties")) {
            it.load(in);
        }
        assertThat(it.getProperty("country"), not(equalTo("中国")));
    }

    @Test
    public void loadPropertyFileWithEncodingThatSupportsUnicodeChars() throws IOException {
        Properties it = new Properties();
        try (InputStream in = resourceAsStream("unicode-chars.properties")) {
            it.load(new InputStreamReader(in, UTF_8));
        }
        assertThat(it.getProperty("country"), equalTo("中国"));
    }

    @Test
    public void enableBufferForResourceStream() throws IOException {
        try (InputStream it = resourceAsStream("unicode-chars.properties")) {
            assertThat(it, isA(BufferedInputStream.class));
        }
    }

    private static InputStream resourceAsStream(String resource) throws IOException {
        return resource(resource).openStream();
    }

    private static URL resource(String resource) {
        return requireNonNull(getSystemResource(resource), "'" + resource + "' does not exists in classpath");
    }
}
