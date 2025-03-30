package dev.tehin.tlib.api.menu.features;

import dev.tehin.tlib.core.menu.Menu;

public interface PageableMenu {

    default Menu getBackMenu() {
        return null;
    }

    default boolean isFilterable() {
        return true;
    }

}
