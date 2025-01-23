package dev.tehin.tlib.core.item;

import dev.tehin.tlib.api.menu.action.ActionExecutor;
import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.utilities.item.ItemUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class InteractableItem {

    private final String id;
    private final ItemBuilder builder;
    private final ActionExecutor action;

    public InteractableItem(String id, ItemBuilder builder, ActionExecutor action) {
        this.id = id;
        this.builder = builder;
        this.action = action;
    }

    public InteractableItem(String id, ItemBuilder builder, Class<? extends Menu> menu) {
        this.id = id;
        this.builder = builder;
        this.action = player -> tLib.get().getMenu().open(player, menu);
    }

    public void handle(Player player) {
        action.execute(player);
    }

    public void give(Player player, int slot) {
        ItemStack item = builder.build(player);
        item = ItemUtil.addTag(item, "hotbar-action", id);

        if (slot == -1) {
            player.getInventory().addItem(item);
            return;
        }

        player.getInventory().setItem(slot, item);
    }
}
