package java9;

import org.junit.Test;

import static java9.AccessibilityMatchers.isPrivate;
import static java9.AccessibilityMatchers.isPublic;
import static org.junit.Assert.assertThat;

/**
 * @author i-katas
 * @since 1.0
 */
public class PrivateInterfaceMethodsTest {
    @Test
    public void methodsAccessibility() throws NoSuchMethodException {
        assertThat(AccessibilityMatchers.class.getDeclaredMethod("hasAccessibility", int.class), isPrivate());
        assertThat(AccessibilityMatchers.class.getDeclaredMethod("isPackage"), isPublic());
    }
}
