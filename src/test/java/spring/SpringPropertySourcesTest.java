package spring;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
public class SpringPropertySourcesTest {
    @Autowired
    private ConfigurableEnvironment currentEnvironment;

    @Test
    public void springPropertyFileSupportsUnicodeChars() {
        assertPropertySourceNamed("utf8", supportsUnicodeChars());
        assertPropertySourceNamed("default", not(supportsUnicodeChars()));
    }

    private void assertPropertySourceNamed(String name, Supplier<Matcher<? super String>> matcherSupplier) {
        org.springframework.core.env.PropertySource<?> propertySource = requireNonNull(currentEnvironment.getPropertySources().get(name), "No property source found: " + name);
        assertThat((String) propertySource.getProperty("country"), matcherSupplier.get());
    }

    private static Supplier<Matcher<? super String>> supportsUnicodeChars() {
        return () -> equalTo("中国");
    }

    private static Supplier<Matcher<? super String>> not(Supplier<Matcher<? super String>> matcher) {
        return () -> Matchers.not(matcher.get());
    }

    @Configuration
    @PropertySource(name = "default", value = "classpath:unicode-chars.properties")
    @PropertySource(name = "utf8", value = "classpath:unicode-chars.properties", encoding = "UTF-8")
    static class Config {
    }
}
