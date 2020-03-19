package java9;

import jdk.jshell.JShell;
import jdk.jshell.Snippet;
import jdk.jshell.SnippetEvent;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.List;

import static java.security.AccessController.doPrivileged;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author i-katas
 * @since 1.0
 */
public class JShellTest {
    @Test
    public void evaluate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, PrivilegedActionException {
        JShell shell = JShell.builder().build();

        List<SnippetEvent> events = shell.eval("int a = 1, b = 2;");

        assertThat(events, hasSize(2));
        assertThat(name(events.get(0).snippet()), equalTo("a"));
        assertThat(events.get(0).value(), equalTo("1"));
        assertThat(name(events.get(1).snippet()), equalTo("b"));
        assertThat(events.get(1).value(), equalTo("2"));
    }

    private String name(Snippet snippet) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, PrivilegedActionException {
        Method name = Snippet.class.getDeclaredMethod("name");
        //should opens jdk.jshell/jdk.jshell=ALL-UNNAMED
        name.setAccessible(true);
        return (String) name.invoke(snippet);
    }

}
