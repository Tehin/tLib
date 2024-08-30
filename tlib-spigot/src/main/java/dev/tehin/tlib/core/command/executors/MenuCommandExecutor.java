package dev.tehin.tlib.core.command.executors;

import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.command.args.CommandArgs;
import dev.tehin.tlib.core.menu.Menu;
import org.bukkit.entity.Player;

public class MenuCommandExecutor extends SimpleCommandExecutor {

    private final Class<? extends Menu> menu;

    public MenuCommandExecutor(String path, String permission, Class<? extends Menu> menu) {
        super(path, permission);

        this.menu = menu;
    }

    @Override
    public void execute(CommandArgs args) {
        tLib.get().getMenu().open((Player) args.getSender(), menu);
    }
}
