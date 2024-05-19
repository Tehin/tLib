package dev.tehin.tlib.api;

import dev.tehin.tlib.api.command.manager.CommandManager;
import dev.tehin.tlib.api.configuration.LibConfiguration;
import dev.tehin.tlib.core.CraftLib;
import net.md_5.bungee.api.plugin.Plugin;

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
    static tLib build(Plugin owner, LibConfiguration configuration) {
        return new CraftLib(owner, configuration);
    }

    /**
     * Builds a new {@link tLib} instance to be used
     * @param owner The {@link Plugin} owner
     * @return The {@link tLib} instance to be used
     */
    static tLib build(Plugin owner) {
        return new CraftLib(owner, null);
    }

    /**
     * Get the {@link tLib} instance to be used
     * @return The previously created instance, or null if not created
     */
    static tLib get() {
        return CraftLib.INSTANCE;
    }

    Plugin getOwner();

    CommandManager getCommand();
}
