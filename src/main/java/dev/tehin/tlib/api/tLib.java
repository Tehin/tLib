package dev.tehin.tlib.api;

import dev.tehin.tlib.api.command.manager.CommandManager;
import dev.tehin.tlib.api.menu.manager.MenuManager;
import dev.tehin.tlib.core.CraftLib;
import dev.tehin.tlib.core.menu.Menu;
import org.bukkit.plugin.Plugin;

public interface tLib {

    /**
     * Builds a new {@link tLib} instance to be used
     * @param owner The {@link Plugin} owner
     * @return The {@link tLib} instance to be used
     */
    static tLib build(Plugin owner) {
        return new CraftLib(owner);
    }

    /**
     * Get the {@link tLib} instance to be used
     * @return The previously created instance, or null if not created
     */
    static tLib get() {
        return CraftLib.INSTANCE;
    }

    Plugin getOwner();

    MenuManager getMenu();
    CommandManager getCommand();
}
