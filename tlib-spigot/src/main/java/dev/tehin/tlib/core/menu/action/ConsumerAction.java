package dev.tehin.tlib.core.menu.action;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.function.Consumer;

public class ConsumerAction extends CraftMenuAction {

    public ConsumerAction(Consumer<Player> action) {
        super(ClickType.LEFT, action);
    }
}
