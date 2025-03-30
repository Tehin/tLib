package dev.tehin.tlib.api.menu.action.data;

import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.utilities.MessageUtil;
import net.minemora.nms.NMS;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record ItemData(String name, List<String> lore, Map<String, String> nbts) {

    public static ItemData of(ItemBuilder builder) {
        List<String> lore = builder.getLore().stream()
                .map(MessageUtil::color)
                .toList();

        return new ItemData(MessageUtil.color(builder.getName()), lore, builder.getNBTs());
    }

    public static ItemData of(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        // Ignore the menu action NBT since the builder we use as our cache does not have it,
        // causing the NBT comparison to always return false
        Set<String> ignoredNbtKeys = Set.of("menu-action");

        Map<String, String> allNBTs = NMS.get().getUtil().getAllNBTs(item, true, ignoredNbtKeys);

        return new ItemData(meta.getDisplayName(), meta.getLore(), allNBTs);
    }
}