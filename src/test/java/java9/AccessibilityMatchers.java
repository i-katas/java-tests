package java9;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

import static java.lang.reflect.Modifier.*;

/**
 * @author i-katas
 * @since 1.0
 */
public interface AccessibilityMatchers {


    static Matcher<? super Member> isPrivate() {
        return hasAccessibility(PRIVATE);
    }

    static Matcher<? super Member> isPackage() {
        return hasAccessibility(0);
    }

    static Matcher<? super Member> isProtected() {
        return hasAccessibility(PROTECTED);
    }

    static Matcher<? super Member> isPublic() {
        return hasAccessibility(PUBLIC);
    }

    private static TypeSafeDiagnosingMatcher<Member> hasAccessibility(int modifier) {
        return new TypeSafeDiagnosingMatcher<>() {
            protected boolean matchesSafely(Member item, Description mismatch) {
                if (!isSatisfied(item)) {
                    mismatch.appendValue(item).appendText(" ")
                            .appendText(descriptionOf(item.getModifiers()));
                    return false;
                }
                return true;
            }

            private boolean isSatisfied(Member item) {
                int accessibility = item.getModifiers() & (PUBLIC | PROTECTED | PRIVATE);
                return accessibility == modifier;
            }

            public void describeTo(Description description) {
                description.appendText(descriptionOf(modifier));
            }

            private String descriptionOf(int modifier) {
                StringDescription description = new StringDescription();
                description.appendText("modifier is ");
                description.appendValue(accessibility(modifier));
                return description.toString();
            }

            private String accessibility(int modifiers) {
                return Modifier.isPublic(modifiers) ? "public"
                        : Modifier.isProtected(modifiers) ? "protected"
                        : Modifier.isPrivate(modifiers) ? "private" : "package";
            }
        };
    }
}
