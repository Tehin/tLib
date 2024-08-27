package dev.tehin.tlib.core.menu.action;

import dev.tehin.tlib.api.menu.action.data.ItemData;
import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.manager.MenuManager;
import dev.tehin.tlib.api.tLib;
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

    private ItemData data;

    @Override
    public void execute(MenuManager manager, Player player) {
        if (this.getData() == null) throw new RuntimeException("Tried to execute an inventory action that has no identifier, you MUST set the name with InventoryAction.setData(ActionData)");

        long now = System.currentTimeMillis();
        if (now - last <= tLib.get().getConfig().menus().getClickDelayInMs()) return;

        setLast(now);
        getAction().accept(player);
    }

    @Override
    public boolean equals(ItemData equals) {
        if (equals == null) return false;
        if (equals.name() == null) return false;

        boolean name = getData().name().equals(equals.name());
        boolean lore = compareLore(equals);

        return lore && name;
    }

    @Override
    public boolean equals(MenuAction equals) {
        return equals(equals.getData());
    }

    private boolean compareLore(ItemData equals) {
        List<String> lore1 = getData().lore();
        List<String> lore2 = equals.lore();

        if (lore1 == null || lore2 == null) return false;

        if (lore1.isEmpty() || lore2.isEmpty()) return true;
        if (lore1.size() != lore2.size()) return false;

        for (int i = 0; i < lore1.size(); i++) {
            if (!lore1.get(i).equals(lore2.get(i))) return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return data.name() + " -> " + action;
    }

}
