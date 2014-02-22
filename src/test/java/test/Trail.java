package test;

/**
 * @author Georg Schmidl
 */
public class Trail {

    private static StringBuilder trail = new StringBuilder();

    public static void append(String s) {
        trail.append(s);
    }

    public static String get() {
        return trail.toString();
    }

    public static void clear() {
        trail = new StringBuilder();
    }
}
