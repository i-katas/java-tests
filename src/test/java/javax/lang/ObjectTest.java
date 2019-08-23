package javax.lang;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author xiaoliang
 * @since 2019-08-23
 */
public class ObjectTest {

    /**
     * For example: {@code List<String>} is the static type of the local variable `it`, but the actual type after type erasure is {@code List}.
     *
     * @see Object#getClass()
     * @see <a href="https://stackoverflow.com/questions/19332856/what-is-meant-by-the-erasure-of-the-static-type-of-the-expression-on-which-it-i">the erasure of the static type of the expression</a>
     * @see <a href="https://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.6">Type Erasure</a>
     *
     */
    @Test
    public void eraseStaticTypeExpressionForClass() {
        List<String> it = new ArrayList<>();

        //Class<? extends List<String>> actualClass = it.getClass(); //compile error
        Class<? extends List> erasedClass = it.getClass();

        assertThat(erasedClass.getName(), equalTo("java.util.ArrayList"));
    }
}
