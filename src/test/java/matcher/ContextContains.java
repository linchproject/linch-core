package matcher;

import org.mockito.ArgumentMatcher;

import java.util.Map;

/**
 * Checks if a context map contains a key with a certain value.
 *
 * @author Georg Schmidl
 */
public class ContextContains extends ArgumentMatcher<Map<String, Object>> {

    private final String key;
    private final Object value;

    public ContextContains(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean matches(Object argument) {
        Map<String, Object> context = (Map<String, Object>) argument;
        return value != null? value.equals(context.get(key)): context.get(key) == null;
    }
}
