package dev.tehin.tlib.core.item;

import dev.tehin.tlib.api.item.ItemManager;
import dev.tehin.tlib.utilities.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CraftItemManager implements ItemManager {

    private final Map<String, InteractableItem> items = new HashMap<>();

    // For performance, do not check the NBT tag of every item clicked, just for certain candidates
    private final Set<Material> candidates = new HashSet<>();

    @Override
    public void register(InteractableItem... items) {
        for (InteractableItem item : items) {
            this.items.put(item.getId(), item);
            candidates.add(item.getBuilder().material());
        }
    }

    @Override
    public void give(Player player, String itemId) {
        give(player, itemId, -1);
    }

    @Override
    public void give(Player player, String itemId, int slot) {
        Optional<InteractableItem> interactableItem = get(itemId);
        if (interactableItem.isEmpty()) {
            throw new IllegalArgumentException("Tried to give an item that does not exist: " + itemId);
        }

        interactableItem.get().give(player, slot);
    }

    @Override
    public Optional<InteractableItem> get(String itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Optional<InteractableItem> get(ItemStack item) {
        // Performance, do not check the NBT tag of every item clicked
        if (!candidates.contains(item.getType())) return Optional.empty();

        Optional<String> action = ItemUtil.getTag(item, "action");
        return action.map(items::get);
    }

}
