package dev.tehin.tlib.api;

import dev.tehin.tlib.api.menu.manager.MenuManager;
import dev.tehin.tlib.core.CraftLib;
import org.bukkit.plugin.Plugin;

public interface tLib {

    static tLib build(Plugin owner) {
        return new CraftLib(owner);
    }

    static tLib get() {
        return CraftLib.INSTANCE;
    }

    Plugin getOwner();

    MenuManager getMenu();
}
