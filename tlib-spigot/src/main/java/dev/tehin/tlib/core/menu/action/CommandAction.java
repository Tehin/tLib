package dev.tehin.tlib.core.menu.action;

import dev.tehin.tlib.utilities.task.TaskUtil;
import org.bukkit.event.inventory.ClickType;

public class CommandAction extends CraftMenuAction {

    public CommandAction(String command) {
        super(ClickType.LEFT, (player) -> {
            TaskUtil.runSync(() -> {
                player.chat("/" + command);
                player.closeInventory();
            });
        });
    }
}
