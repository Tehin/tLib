package dev.tehin.tlib.api.item;

import dev.tehin.tlib.core.item.InteractableItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface ItemManager {

    void register(InteractableItem... items);

    void give(Player player, String item);
    void give(Player player, String item, int slot);

    Optional<InteractableItem> get(String itemId);
    Optional<InteractableItem> get(ItemStack item);
}
