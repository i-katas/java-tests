package java7;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

/**
 * @author xiaoliang
 * @since 2019-08-25
 */
public class TryWithResourcesBlockTest {
    private final InputStream in = mock(InputStream.class);
    private final OutputStream out = mock(OutputStream.class);

    /**
     * An exception is thrown from the {@code try} block and one or more exceptions are thrown from the {@code try-with-resources} statement, then those exceptions thrown from the {@code try-with-resources} statement are suppressed.
     *
     * @see Throwable#addSuppressed(Throwable)
     * @see <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html#suppressed-exceptions">Suppressed Exceptions</a>
     */
    @Test
    public void accumulateSuppressedExceptions() throws IOException {
        IOException readError = new IOException("EOF");
        IOException readCloseError = new IOException("close failed");
        IOException writerCloseError = new IOException("close failed");

        doThrow(readError).when(in).read();
        doThrow(readCloseError).when(in).close();
        doThrow(writerCloseError).when(out).close();

        try (InputStream reader = in; OutputStream writer = out) {
            writer.write(reader.read());
        } catch (IOException expected) {
            assertThat(expected, is(readError));
            assertThat(expected.getSuppressed(), hasItemInArray(is(readCloseError)));
            assertThat(expected.getSuppressed(), hasItemInArray(is(writerCloseError)));
        }
    }
}
