package dev.tehin.tlib.core.hotbar;

import dev.tehin.tlib.api.hotbar.HotbarItemCondition;
import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.item.InteractableItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class Hotbar {

    private final String id;
    private final List<HotbarItem> items = new ArrayList<>();

    public void add(HotbarAlignment slot, String item, HotbarItemCondition condition) {
        Optional<InteractableItem> interactable = tLib.get().getItem().get(item);

        if (interactable.isEmpty()) {
            throw new IllegalArgumentException("Tried to add an item that does not exist: " + item);
        }

        this.items.add(new HotbarItem(item, slot, interactable.get(), condition));
    }

    public void give(Player player) {
        for (HotbarItem item : items) {
            if (item.getCondition() != null && !item.getCondition().canGive(player)) continue;

            giveSingleItem(player, item);
        }
    }

    public void giveSingleItem(Player player, HotbarItem item) {
        HotbarAlignment alignment = item.getAlignment();
        int slot = alignment.getSlot();

        if (slot == -1) {
            slot = switch (alignment) {
                case LEFT -> -1; // -1 uses Inventory.addItem(ItemStack), which centers it on the left
                case RIGHT -> {
                    // Find first empty hotbar slot from the right
                    for (int i = 8; i >= 0; i--) {
                        if (player.getInventory().getItem(i) == null) yield i;
                    }

                    yield -1; // There is no space in the hotbar, just add the item man
                }
                default -> throw new IllegalArgumentException("Invalid alignment: " + alignment);
            };
        }

        item.getInteractable().give(player, slot);
    }
}
