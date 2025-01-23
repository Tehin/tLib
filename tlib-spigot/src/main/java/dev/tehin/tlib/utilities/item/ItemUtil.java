package dev.tehin.tlib.utilities.item;

import dev.tehin.tlib.api.lang.LangProvider;
import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.lang.LangParser;
import dev.tehin.tlib.core.menu.MenuContentBuilder;
import net.minemora.nms.NMS;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemUtil {

    public static boolean isArmor(Material material) {
        String[] pieces = {"chestplate", "leggings", "helmet", "boots"};

        return Arrays.stream(pieces).anyMatch(piece -> material.name().toLowerCase().contains(piece));
    }

    public static boolean isSword(Material material) {
        return material.name().contains("SWORD");
    }

    public static void addGlow(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
    }

    public static ItemStack addTag(ItemStack item, String tag, String content) {
        return NMS.get().getUtil().setNBT(item, tag, content);
    }

    public static Optional<String> getTag(ItemStack item, String tag) {
        return NMS.get().getUtil().getNBT(item, tag);
    }

    public static void addColor(DyeColor color, ItemStack... leatherArmors) {
        for (ItemStack leatherArmor : leatherArmors) {
            if (leatherArmor == null || !leatherArmor.getType().name().contains("LEATHER")) continue;

            LeatherArmorMeta meta = (LeatherArmorMeta) leatherArmor.getItemMeta();
            meta.setColor(color.getColor());

            leatherArmor.setItemMeta(meta);
        }
    }

    public static ItemStack addEnchant(ItemStack item, Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);

        return item;
    }

    @Deprecated(forRemoval = true)
    public static void fill(List<ItemStack> items) {
        while (items.size() % 9 != 0) {
            items.add(null);
        }
    }

    @Deprecated(forRemoval = true)
    public static void addEmptyRow(List<ItemStack> items) {
        for (int i = 1; i <= 9; i++) items.add(null);
    }

    @Deprecated(forRemoval = true)
    public static void addGlassRow(List<ItemStack> items) {
        for (int i = 1; i <= 9; i++) items.add(ItemDefaults.getGlass());
    }

    @Deprecated(forRemoval = true)
    public static void addGlassRowWithMiddle(List<ItemStack> items, ItemStack middle) {
        for (int i = 1; i <= 9; i++) {
            if (i == 5) {
                items.add(middle);
                continue;
            }

            items.add(ItemDefaults.getGlass());
        }
    }

    @Deprecated(forRemoval = true)
    public static void addItemInMiddle(List<ItemStack> items, ItemStack middle) {
        for (int i = 1; i <= 9; i++) {
            if (i == 5) items.add(middle);
            else items.add(null);
        }
    }

    @Deprecated(forRemoval = true)
    public static void addWithSpaces(List<ItemStack> items, ItemStack... toAdd) {
        int length = toAdd.length;

        switch (length) {
            case 1: {
                for (int i = 0; i < 4; i++) items.add(null);
                items.add(toAdd[0]);
                for (int i = 0; i < 4; i++) items.add(null);
                return;
            }
            case 2: {
                for (int i = 0; i < 3; i++) items.add(null);
                items.add(toAdd[0]);
                items.add(null);
                items.add(toAdd[1]);
                for (int i = 0; i < 3; i++) items.add(null);
                return;
            }
            case 3: {
                for (int i = 0; i < 2; i++) items.add(null);
                items.add(toAdd[0]);
                items.add(null);
                items.add(toAdd[1]);
                items.add(null);
                items.add(toAdd[2]);
                for (int i = 0; i < 2; i++) items.add(null);
                return;
            }
            case 4: {
                items.add(null);
                items.add(toAdd[0]);
                items.add(null);
                items.add(toAdd[1]);
                items.add(null);
                items.add(toAdd[2]);
                items.add(null);
                items.add(toAdd[3]);
                items.add(null);
                return;
            }
            case 5: {
                items.add(null);
                items.add(toAdd[0]);
                items.add(toAdd[1]);
                items.add(null);
                items.add(toAdd[2]);
                items.add(null);
                items.add(toAdd[3]);
                items.add(toAdd[4]);
                items.add(null);
                return;
            }
            default: {
                items.addAll(Arrays.stream(toAdd).collect(Collectors.toList()));
            }
        }
    }

    public boolean isEqual(ItemStack s1, ItemStack s2) {
        if (!s1.getItemMeta().getDisplayName().equals(s2.getItemMeta().getDisplayName())) return false;

        List<String> lore1 = s1.getItemMeta().getLore();
        List<String> lore2 = s2.getItemMeta().getLore();

        boolean lore = false;
        for (int i = 0; i < lore1.size(); i++) {
            boolean empty = lore2.isEmpty();
            boolean differentSizes = lore1.size() != lore2.size();

            if (empty || differentSizes) break;

            lore = lore1.get(i).equals(lore2.get(i));
            if (!lore) break;
        }

        return lore;
    }

    @Deprecated(forRemoval = true)
    public static void fillRow(MenuContentBuilder builder, ItemBuilder item) {
        for (int i = 0; i < 9; i++) {
            builder.add(item);
        }
    }

    @Deprecated(forRemoval = true)
    public static void fillRowWithMiddle(MenuContentBuilder builder, ItemBuilder filler, ItemBuilder middle) {
        for (int i = 0; i < 9; i++) {
            if (i == 4) {
                builder.add(middle);
                continue;
            }

            builder.add(filler);
        }
    }

    public static int getArmorSlotByMaterial(Material material) {
        String name = material.name();

        if (name.contains("HELMET")) {
            return 3;
        }

        if (name.contains("CHESTPLATE")) {
            return 2;
        }

        if (name.contains("LEGGINGS")) {
            return 1;
        }

        if (name.contains("BOOTS")) {
            return 0;
        }

        return -1;
    }

    public static void applyLang(ItemStack stack, Player player) {
        LangProvider provider = tLib.get().getConfig().langProvider();
        if (provider == null) return;

        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return;

        meta.setDisplayName(LangParser.parse(player, meta.getDisplayName()));

        List<String> oldLore = stack.getItemMeta().getLore();
        if (oldLore != null) {
            List<String> newLore = new ArrayList<>();

            for (String loreLine : oldLore) {
                newLore.add(LangParser.parse(player, loreLine));
            }

            meta.setLore(newLore);
        }

        stack.setItemMeta(meta);
    }

}
