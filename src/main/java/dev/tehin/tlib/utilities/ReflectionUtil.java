package dev.tehin.tlib.utilities;

public class ReflectionUtil {

    public static boolean isChild(Class<?> parent, Class<?> child) {
        return parent.isAssignableFrom(child);
    }

}
