package dev.tehin.tlib.core.item;

import dev.tehin.tlib.utilities.MessageUtil;
import dev.tehin.tlib.utilities.item.ItemUtil;
import lombok.Getter;
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
@Getter
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

        base.setItemMeta(meta);

        if (glow) base = ItemUtil.addGlow(base);

        return base;
    }

    public void apply(ItemStack found) {
        ItemMeta meta = found.getItemMeta();

        if (name != null) {
            meta.setDisplayName(MessageUtil.color(name));
        }

        if (lore.length > 0) {
            meta.setLore(Arrays.stream(lore).map(MessageUtil::color).collect(Collectors.toList()));
        }

        found.setDurability((short) data);

        if (color != null && material.name().toUpperCase().contains("LEATHER")) {
            LeatherArmorMeta leather = (LeatherArmorMeta) meta;
            leather.setColor(color.getColor());
        }

        found.setItemMeta(meta);

        if (glow) found = ItemUtil.addGlow(found);
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public int getData() {
        return data;
    }

    public int getAmount() {
        return amount;
    }

    public String[] getLore() {
        return lore;
    }

    public DyeColor getColor() {
        return color;
    }

    public boolean isGlow() {
        return glow;
    }
}
