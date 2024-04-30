package dev.tehin.tlib.api.menu.action;

import dev.tehin.tlib.api.menu.action.data.ActionData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.function.Consumer;

public interface MenuAction {

    ClickType getType();
    Consumer<Player> getAction();

    int getId();
    void setId(int id);

    ActionData getData();
    void setData(ActionData data);

    void execute(Player player);
    boolean equals(MenuAction comparator);
}
