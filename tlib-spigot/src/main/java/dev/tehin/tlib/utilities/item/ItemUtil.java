package dev.tehin.tlib.utilities.item;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemUtil {

    public static boolean isArmor(Material material) {
        String[] pieces = {"chestplate", "leggings", "helmet", "boots"};

        return Arrays.stream(pieces).anyMatch(piece -> material.name().toLowerCase().contains(piece));
    }

    public static ItemStack addGlow(ItemStack item){
        // TODO: Mover a otro lugar para que sea util en todas las versiones?
        try {
            net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
            NBTTagCompound tag = null;

            if (!nmsStack.hasTag()) {
                tag = new NBTTagCompound();
                nmsStack.setTag(tag);
            }

            if (tag == null) tag = nmsStack.getTag();

            NBTTagList ench = new NBTTagList();
            tag.set("ench", ench);
            nmsStack.setTag(tag);

            return CraftItemStack.asCraftMirror(nmsStack);
        } catch (Exception ignored) {
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            return item;
        }
    }

    public static ItemStack addTag(ItemStack item, String tag, String content) {
        return NBTEditor.set(item, content, tag);
    }

    public static Optional<String> getTag(ItemStack item, String tag) {
        return NBTEditor.getString(item, tag);
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

    public static void addEmptyRow(List<ItemStack> items) {
        for (int i = 1; i <= 9; i++) items.add(null);
    }

    public static void addGlassRow(List<ItemStack> items) {
        for (int i = 1; i <= 9; i++) items.add(ItemDefaults.getGlass());
    }

    public static void addGlassRowWithMiddle(List<ItemStack> items, ItemStack middle) {
        for (int i = 1; i <= 9; i++) {
            if (i == 5) {
                items.add(middle);
                continue;
            }

            items.add(ItemDefaults.getGlass());
        }
    }

    public static void addItemInMiddle(List<ItemStack> items, ItemStack middle) {
        for (int i = 1; i <= 9; i++) {
            if (i == 5) items.add(middle);
            else items.add(null);
        }
    }

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
}
