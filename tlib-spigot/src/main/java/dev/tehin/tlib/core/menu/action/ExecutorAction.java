package dev.tehin.tlib.core.menu.action;

import dev.tehin.tlib.api.menu.action.ActionExecutor;
import org.bukkit.event.inventory.ClickType;

public class ExecutorAction extends CraftMenuAction {
    public ExecutorAction(ActionExecutor action) {
        super(ClickType.LEFT, action);
    }
}
