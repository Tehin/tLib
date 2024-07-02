package dev.tehin.tlib.utilities.item;

import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.material.ModernMaterialParser;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemDefaults {
    public static ItemStack getGlass() {
        return new ItemBuilder(ModernMaterialParser.getGlassPane()).name("&7").data(7).build();
    }

    public static ItemStack getComingSoon() {
        return new ItemBuilder(Material.INK_SACK).name("&7&lPróximamente...").data(8).build();
    }

    public static ItemStack getLeatherPiece(Material armor, DyeColor color) {
        ItemStack item = new ItemStack(armor);

        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color.getColor());
        meta.addEnchant(Enchantment.DURABILITY, 10, true);

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack[] getLeatherArmor(DyeColor color) {
        ItemStack[] items = new ItemStack[4];
        Material[] materials = {Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET};

        for (int i = 0; i <= 3; i++) {
            items[i] = getLeatherPiece(materials[i], color);
        }

        return items;
    }
}
