package dev.tehin.tlib.core.menu.action;

import dev.tehin.tlib.api.menu.action.ActionExecutor;
import dev.tehin.tlib.core.menu.ConfirmationMenu;

public class ConfirmedExecutorActon extends ExecutorAction {

    public ConfirmedExecutorActon(String display, String description, ActionExecutor onConfirm) {
        this(display, description, onConfirm, null);
    }

    public ConfirmedExecutorActon(String display, String description, ActionExecutor onConfirm, ActionExecutor onCancel) {
        super((clicker) -> {
            new ConfirmationMenu(display, description, onConfirm, onCancel).open(clicker);
        });
    }
}
