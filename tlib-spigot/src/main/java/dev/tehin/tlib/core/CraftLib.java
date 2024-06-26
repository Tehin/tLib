package dev.tehin.tlib.core;

import dev.tehin.tlib.api.configuration.LibConfiguration;
import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.command.CraftCommandManager;
import dev.tehin.tlib.core.menu.manager.CraftMenuManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;

@Getter
public class CraftLib implements tLib {

    public static CraftLib INSTANCE;

    private final Plugin owner;
    private final LibConfiguration config;

    private final CraftMenuManager menu;
    private final CraftCommandManager command;

    @SneakyThrows
    public CraftLib(Plugin owner, LibConfiguration config) {
        INSTANCE = this;

        LibLogger.log("Created instance for: " + owner.getName());

        this.owner = owner;
        this.config = (config == null) ? new LibConfiguration() : config;

        this.menu = new CraftMenuManager();
        this.command = new CraftCommandManager();
    }

    public static void build(Plugin owner, LibConfiguration configuration) {
        new CraftLib(owner, configuration);
    }
}
