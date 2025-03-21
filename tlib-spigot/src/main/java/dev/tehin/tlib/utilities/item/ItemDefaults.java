package dev.tehin.tlib.utilities.item;

import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.core.menu.action.ErrorAction;
import dev.tehin.tlib.core.menu.action.ExecutorAction;
import dev.tehin.tlib.utilities.chat.LoreUtil;
import net.minemora.nms.NMS;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemDefaults {

    public static ItemBuilder GLASS = new ItemBuilder(NMS.Material.STAINED_GLASS_PANE).data(0).name("&7");

    public static ItemBuilder BACK(Menu back) {
        return new ItemBuilder(NMS.Material.WATCH)
//                .data(1)
                .name("&c&lVolver")
                .lore(LoreUtil.split("Regresa al anterior menú"))
                .action(new ExecutorAction(back::open));
    }

    public static ItemBuilder BACK(Class<? extends Menu> back) {
        return BACK(tLib.get().getMenu().getMenu(back));
    }

    public static ItemBuilder BLOCKED = new ItemBuilder(NMS.Material.BARRIER)
            .name("&8&l???")
            .action(new ErrorAction());

    public static ItemStack getGlass() {
        return new ItemBuilder(NMS.Material.STAINED_GLASS_PANE).name("&7").data(7).build();
    }

    public static ItemStack getComingSoon() {
        return new ItemBuilder(NMS.Material.INK_SACK).name("&7&lPróximamente...").data(8).build();
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
        Material[] materials = {
                NMS.Material.LEATHER_BOOTS,
                NMS.Material.LEATHER_LEGGINGS,
                NMS.Material.LEATHER_CHESTPLATE,
                NMS.Material.LEATHER_HELMET
        };

        for (int i = 0; i <= 3; i++) {
            items[i] = getLeatherPiece(materials[i], color);
        }

        return items;
    }
}
