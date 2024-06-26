package dev.tehin.tlib.core.exceptions;

import dev.tehin.tlib.core.menu.Menu;

public class NoPropertiesFoundException extends Exception {

    public NoPropertiesFoundException(Class<?> clazz) {
        super("Not properties annotation was found on initialization, see: https://docs.com/annotations (On: " + clazz + ")");
    }
}
