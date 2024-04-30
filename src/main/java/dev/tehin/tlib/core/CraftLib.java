package dev.tehin.tlib.core;

import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.menu.manager.CraftMenuManager;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

@Getter
public class CraftLib implements tLib {

    public static CraftLib INSTANCE;

    private final Plugin owner;
    private final CraftMenuManager menu;

    public CraftLib(Plugin owner) {
        INSTANCE = this;

        this.owner = owner;
        this.menu = new CraftMenuManager();
    }
}
