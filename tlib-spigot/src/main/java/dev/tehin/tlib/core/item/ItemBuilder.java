package dev.tehin.tlib.core.item;

import dev.tehin.tlib.api.menu.action.MenuAction;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Accessors(fluent = true, chain = true)
@Setter
@Getter
public class ItemBuilder {

    private final Material material;
    private String name = null;
    private int data = 0, amount = 1;
    private List<String> lore = new ArrayList<>();
    private DyeColor color = null;
    private boolean glow = false;
    private MenuAction action = null;

    public ItemStack build() {
        ItemStack base = new ItemStack(material, amount, (short) data);
        setMeta(base);

        return base;
    }

    public void apply(ItemStack found) {
        setMeta(found);
    }

    private void setMeta(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        if (name != null) {
            meta.setDisplayName(MessageUtil.color(name));
        }

        if (!lore.isEmpty()) {
            meta.setLore(lore.stream().map(MessageUtil::color).collect(Collectors.toList()));
        }

        if (color != null && material.name().toUpperCase().contains("LEATHER")) {
            LeatherArmorMeta leather = (LeatherArmorMeta) meta;
            leather.setColor(color.getColor());
        }

        item.setItemMeta(meta);

        if (glow) ItemUtil.addGlow(item);
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

    public List<String> getLore() {
        return lore;
    }

    public DyeColor getColor() {
        return color;
    }

    public boolean isGlow() {
        return glow;
    }

    public MenuAction getAction() {
        return action;
    }

}
