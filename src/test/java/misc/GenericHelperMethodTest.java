package misc;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author xiaoliang
 * @since 2019-08-24
 */
public class GenericHelperMethodTest {

    @Test
    public void swapElementsForUpperBoundedParameterizedType() {
        Point<Integer> point = new Point<>(1, 3);
        assertThat(point.x, equalTo(1));
        assertThat(point.y, equalTo(3));

        rotate(point);

        assertThat(point.x, equalTo(3));
        assertThat(point.y, equalTo(1));
    }

    /**
     * The wildcard capture error raised by compiler if inline {@link GenericHelperMethodTest#rotateHelper} method.
     *
     * @param point
     * @see <a href="Wildcard Capture and Helper Methods">https://docs.oracle.com/javase/tutorial/java/generics/capture.html</a>
     */
    private void rotate(Point<?> point) {
        rotateHelper(point);
    }

    private <T extends Number> void rotateHelper(Point<T> p) {
        T x = p.x;
        p.x = p.y;
        p.y = x;
    }

    static class Point<T extends Number> {
        T x, y;

        Point(T x, T y) {
            this.x = x;
            this.y = y;
        }
    }
}
