package dev.tehin.tlib.api.menu.action.data;

import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.utilities.MessageUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public record ItemData(String name, List<String> lore) {

    public static ItemData of(ItemBuilder builder) {
        return new ItemData(builder.getName(), builder.getLore());
    }

    public static ItemData of(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        return new ItemData(meta.getDisplayName(), meta.getLore());
    }
}