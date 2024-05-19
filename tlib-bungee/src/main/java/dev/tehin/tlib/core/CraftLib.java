package dev.tehin.tlib.core;

import dev.tehin.tlib.api.configuration.LibConfiguration;
import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.command.CraftCommandManager;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class CraftLib implements tLib {

    public static CraftLib INSTANCE;

    private final Plugin owner;
    private final LibConfiguration configuration;

    private final CraftCommandManager command;

    public CraftLib(Plugin owner, LibConfiguration configuration) {
        INSTANCE = this;

        this.owner = owner;
        this.configuration = configuration;

        this.command = new CraftCommandManager();
    }
}
