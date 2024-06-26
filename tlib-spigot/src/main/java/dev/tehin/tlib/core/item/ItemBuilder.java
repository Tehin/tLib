package dev.tehin.tlib.core.item;

import dev.tehin.tlib.utilities.MessageUtil;
import dev.tehin.tlib.utilities.item.ItemUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Accessors(fluent = true, chain = true)
@Setter
public class ItemBuilder {

    private final Material material;
    private String name = null;
    private int data = 0, amount = 1;
    private String[] lore = new String[0];
    private DyeColor color = null;
    private boolean glow = false;

    public ItemStack build() {
        ItemStack base = new ItemStack(material, amount, (short) data);
        ItemMeta meta = base.getItemMeta();

        if (name != null) {
            meta.setDisplayName(MessageUtil.color(name));
        }

        if (lore.length > 0) {
            meta.setLore(Arrays.stream(lore).map(MessageUtil::color).collect(Collectors.toList()));
        }

        if (color != null && material.name().toUpperCase().contains("LEATHER")) {
            LeatherArmorMeta leather = (LeatherArmorMeta) meta;
            leather.setColor(color.getColor());
        }

        if (glow) ItemUtil.addGlow(base);

        base.setItemMeta(meta);
        return base;
    }
}
