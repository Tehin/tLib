package dev.tehin.tlib.api;

import dev.tehin.tlib.api.command.manager.CommandManager;
import dev.tehin.tlib.api.configuration.LibConfiguration;
import dev.tehin.tlib.api.hotbar.HotbarManager;
import dev.tehin.tlib.api.item.ItemManager;
import dev.tehin.tlib.api.menu.manager.MenuManager;
import dev.tehin.tlib.core.CraftLib;
import org.bukkit.plugin.Plugin;

public interface tLib {

    /**
     * Builds a new {@link tLib} instance to be used with a configuration
     * <br/> <br/>
     * The configuration defines the library behavior, for no configuration:
     * <br/>
     * See: {@link tLib#build(Plugin)}
     * @param owner The {@link Plugin} owner
     * @param configuration The base configuration for the library
     * @return The {@link tLib} instance to be used with the base configuration
     */
    static void build(Plugin owner, LibConfiguration configuration) {
        CraftLib.build(owner, configuration);
    }

    /**
     * Builds a new {@link tLib} instance to be used
     * @param owner The {@link Plugin} owner
     */
    static void build(Plugin owner) {
        build(owner, null);
    }

    /**
     * @return The previous {@link tLib} instance created
     */
    static tLib get() {
        return CraftLib.INSTANCE;
    }

    Plugin getOwner();

    LibConfiguration getConfig();
    MenuManager getMenu();
    CommandManager getCommand();
    ItemManager getItem();
    HotbarManager getHotbar();

}
