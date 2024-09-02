package dev.tehin.tlib.core.menu.action;

import dev.tehin.tlib.api.menu.action.ActionExecutor;
import dev.tehin.tlib.core.menu.ConfirmationMenu;

public class ConfirmedExecutorActon extends ExecutorAction {

    public ConfirmedExecutorActon(String display, String description, ActionExecutor action) {
        super((clicker) -> {
            new ConfirmationMenu(display, description, action).open(clicker);
        });
    }
}
