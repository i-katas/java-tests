package org.springframework.context.config;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author xiaoliang
 * @since 2019-08-23
 */
@RunWith(SpringRunner.class)
public class SpringPropertySourcesTest {
    @Autowired
    private ConfigurableEnvironment currentEnvironment;

    @Test
    public void springPropertyFileSupportsUnicodeChars() {
        assertPropertySourceNamed("utf8", supportsUnicodeChars());
        assertPropertySourceNamed("default", not(supportsUnicodeChars()));
    }

    private void assertPropertySourceNamed(String name, Function<Matcher<? super Object>, Matcher<? super Object>> expectation) {
        org.springframework.core.env.PropertySource<?> propertySource = requireNonNull(currentEnvironment.getPropertySources().get(name), "No property source found: " + name);
        assertThat(propertySource.getProperty("country"), expectation.apply(equalTo("中国")));
    }

    private static <T extends Matcher<? super String>> Function<T, T> supportsUnicodeChars() {
        return Function.identity();
    }

    private static <T> Function<Matcher<? super T>, Matcher<? super T>> not(Function<Matcher<? super T>, Matcher<? super T>> matcher) {
        return t -> Matchers.not(matcher.apply(t));
    }

    @Configuration
    @PropertySource(name = "default", value = "classpath:unicode-chars.properties")
    @PropertySource(name = "utf8", value = "classpath:unicode-chars.properties", encoding = "UTF-8")
    static class Config {
    }
}
