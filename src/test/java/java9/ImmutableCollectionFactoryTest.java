package java9;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;

/**
 * @author i-katas
 * @since 1.0
 */
public class ImmutableCollectionFactoryTest {
    @Test
    public void immutableRandomSet() {
        Set<Integer> ids = Set.of(1, 2);

        assertThat(ids, containsInAnyOrder(1, 2));
        assertThrows(UnsupportedOperationException.class, ids::clear);
    }

    @Test
    public void immutableList() {
        List<Integer> ids = List.of(1, 2);

        assertThat(ids, contains(1, 2));
        assertThrows(UnsupportedOperationException.class, ids::clear);
    }

    @Test
    public void immutableRandomMap() {
        Map<String, String> pairs = Map.of("foo", "bar", "key", "value");

        assertThat(pairs, aMapWithSize(2));
        assertThat(pairs, hasEntry("foo", "bar"));
        assertThat(pairs, hasEntry("key", "value"));
        assertThrows(UnsupportedOperationException.class, pairs::clear);
    }
}
