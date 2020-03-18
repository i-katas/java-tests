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
        Point<?> point = new Point<>(1, 3);
        assertThat(point.x, equalTo(1));
        assertThat(point.y, equalTo(3));

        //point.x = point.y; //error
        swap(point);

        assertThat(point.x, equalTo(3));
        assertThat(point.y, equalTo(1));
    }

    /**
     * The wildcard capture error raised by compiler if inline {@link GenericHelperMethodTest#swap} method.
     *
     * @param point
     * @see <a href="Wildcard Capture and Helper Methods">https://docs.oracle.com/javase/tutorial/java/generics/capture.html</a>
     */
    private <T> void swap(Point<T> point) {
        T x = point.x;
        point.x = point.y;
        point.y = x;
    }

    static class Point<T> {
        T x, y;

        Point(T x, T y) {
            this.x = x;
            this.y = y;
        }
    }
}
