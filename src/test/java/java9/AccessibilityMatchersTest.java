package java9;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Member;

import static java9.AccessibilityMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author i-katas
 * @since 1.0
 */
public class AccessibilityMatchersTest {
    private int _private;
    public int _public;
    protected int _protected;
    int _default;

    @Test
    public void matches() throws NoSuchFieldException {
        assertThat(field("_private"), isPrivate());
        assertThat(field("_public"), not(isPrivate()));
        assertThat(field("_default"), not(isPrivate()));

        assertThat(field("_public"), isPublic());
        assertThat(field("_default"), not(isPublic()));

        assertThat(field("_default"), isPackage());
        assertThat(field("_private"), not(isPackage()));

        assertThat(field("_protected"), isProtected());
        assertThat(field("_default"), not(isProtected()));
    }

    @Test
    public void diagnosticMessageWhenMismatched() throws NoSuchFieldException {
        assertDiagnosticMessageOf(field("_private"), is(blankString()));
        assertDiagnosticMessageOf(field("_public"), equalTo("<" + field("_public") + "> modifier is \"public\""));
        assertDiagnosticMessageOf(field("_default"), equalTo("<" + field("_default") + "> modifier is \"package\""));
    }

    private void assertDiagnosticMessageOf(Member field, Matcher<String> matcher) {
        StringDescription description = new StringDescription();
        isPrivate().describeMismatch(field, description);
        assertThat(description.toString(), matcher);
    }

    @Test
    public void description() {
        assertDescriptionOf(isPrivate(), equalTo("modifier is \"private\""));
        assertDescriptionOf(isPublic(), equalTo("modifier is \"public\""));
        assertDescriptionOf(isPackage(), equalTo("modifier is \"package\""));
        assertDescriptionOf(isProtected(), equalTo("modifier is \"protected\""));
    }

    private void assertDescriptionOf(Matcher<? super Member> matcher, Matcher<String> descriptionMatcher) {
        StringDescription description = new StringDescription();

        matcher.describeTo(description);

        assertThat(description.toString(), descriptionMatcher);
    }

    private Field field(String publicly) throws NoSuchFieldException {
        return getClass().getDeclaredField(publicly);
    }
}
