package dev.tehin.tlib.core.item;

import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.utilities.item.ItemUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Getter
public class InteractableItem {

    private final String id;
    private final ItemBuilder builder;
    private final Consumer<Player> action;

    private ItemStack result;

    public InteractableItem(String id, ItemBuilder builder, Consumer<Player> action) {
        this.id = id;
        this.builder = builder;
        this.action = action;
    }

    public InteractableItem(String id, ItemBuilder builder, Class<? extends Menu> menu) {
        this.id = id;
        this.builder = builder;
        this.action = player -> tLib.get().getMenu().open(player, menu);
    }

    private void create() {
        ItemStack built = builder.build();

        result = ItemUtil.addTag(built, "action", id);
    }

    public void handle(Player player) {
        action.accept(player);
    }

    public void give(Player player, int slot) {
        if (result == null) create();

        if (slot == -1) {
            player.getInventory().addItem(result);
            return;
        }

        player.getInventory().setItem(slot, result);
    }
}
