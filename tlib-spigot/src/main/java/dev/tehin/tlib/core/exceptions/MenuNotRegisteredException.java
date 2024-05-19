package dev.tehin.tlib.core.exceptions;

import dev.tehin.tlib.core.menu.Menu;

public class MenuNotRegisteredException extends Exception {

    public MenuNotRegisteredException(Class<? extends Menu> clazz) {
        super("Tried to retrieve a non-registered Menu: " + clazz);
    }

}
