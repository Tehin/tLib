package dev.tehin.tlib.core.menu.action;

import dev.tehin.tlib.api.menu.action.data.ActionData;
import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.manager.MenuManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;
import java.util.function.Consumer;

@Getter
@Setter
@RequiredArgsConstructor
public class CraftMenuAction implements MenuAction {
    private final ClickType type;
    private final Consumer<Player> action;

    private int id;
    private long last;

    private ActionData data;

    @Override
    public void execute(MenuManager manager, Player player) {
        if (this.getData() == null) throw new RuntimeException("Tried to execute an inventory action that has no identifier, you MUST set the name with InventoryAction.setData(ActionData)");

        long now = System.currentTimeMillis();
        if (now - last <= manager.getLib().getConfig().menus().getClickDelayInMs()) return;

        setLast(now);
        getAction().accept(player);
    }

    @Override
    public boolean equals(MenuAction equals) {
        if (equals.getData() == null) return false;
        if (equals.getData().getName() == null) return false;

        boolean name = getData().getName().equals(equals.getData().getName());
        boolean lore = compareLore(equals);

        return lore && name;
    }

    private boolean compareLore(MenuAction equals) {
        boolean lore = false;
        for (int i = 0; i < getData().getLore().size(); i++) {
            List<String> thisLore = getData().getLore();
            List<String> equalsLore = equals.getData().getLore();

            boolean nil = equalsLore == null;
            if (nil) break;

            boolean empty = thisLore.isEmpty() || equalsLore.isEmpty();
            boolean differentSizes = thisLore.size() != equalsLore.size();
            if (empty || differentSizes) break;

            lore = thisLore.get(i).equals(equalsLore.get(i));
            if (!lore) break;
        }

        return lore;
    }

    @Override
    public String toString() {
        return data.getName() + " -> " + action;
    }

}
