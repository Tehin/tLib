package dev.tehin.tlib.core.item;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.utilities.MessageUtil;
import dev.tehin.tlib.utilities.chat.LoreUtil;
import dev.tehin.tlib.utilities.inventory.ItemBuilderProvider;
import dev.tehin.tlib.utilities.item.ItemUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minemora.nms.NMS;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Accessors(fluent = true, chain = true)
@Setter
public class ItemBuilder implements ItemBuilderProvider {

    private final Material material;
    private String name = null;
    private int data = 0, amount = 1;
    private List<String> lore = new ArrayList<>();
    private DyeColor color = null;
    private boolean glow = false;
    private MenuAction action = null;

    @Setter(AccessLevel.NONE)
    private List<ItemFlag> flags = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    private Map<Enchantment, Integer> enchants = null;

    @Setter(AccessLevel.NONE)
    private List<Pattern> patterns = null;

    private DyeColor baseColor = null;

    public ItemStack build() {
        ItemStack base = NMS.get().getItemStackCreator().create(material).getItemStack();
        base.setAmount(amount);

        setProperties(base);

        return base;
    }

    public void apply(ItemStack found) {
        setProperties(found);
    }

    private void setProperties(ItemStack item) {
        NMS.get().getUtil().setItemStackData(item, (short) data);

        ItemMeta meta = item.getItemMeta();

        if (enchants != null) {
            // If we are adding enchants to an enchanted book, we need to use the EnchantmentStorageMeta
            if (meta instanceof EnchantmentStorageMeta enchantMeta) {
                for (Map.Entry<Enchantment, Integer> enchantment : enchants.entrySet()) {
                    enchantMeta.addStoredEnchant(enchantment.getKey(), enchantment.getValue(), true);
                }
            } else {
                // If not, add the enchants to the item directly
                enchants.forEach(item::addUnsafeEnchantment);
            }
        }

        if (meta instanceof BannerMeta bannerMeta) {
            if (baseColor != null) NMS.get().getItemStackCreator().fromStack(item).setBaseColor(baseColor);

            if (patterns != null) patterns.forEach(bannerMeta::addPattern);
        }

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

        if (!flags.isEmpty()) meta.addItemFlags(flags.toArray(new ItemFlag[0]));
        item.setItemMeta(meta);

        if (glow) ItemUtil.addGlow(item);
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        if (enchants == null) enchants = new HashMap<>();

        enchants.put(enchantment, level);
        return this;
    }

    public ItemBuilder addPattern(Pattern pattern) {
        if (patterns == null) patterns = new ArrayList<>();

        patterns.add(pattern);
        return this;
    }

    public ItemBuilder lore(String lore) {
        this.lore = LoreUtil.split(lore);

        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore = lore;

        return this;
    }

    public ItemBuilder addFlag(ItemFlag flag) {
        this.flags.add(flag);

        return this;
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

    public DyeColor getBaseColor() {
        return baseColor;
    }

    @Override
    public ItemBuilder clone() {
        ItemBuilder item = new ItemBuilder(material)
                .data(data)
                .amount(amount)
                .name(name)
                .lore(lore)
                .color(color)
                .glow(glow)
                .action(action)
                .baseColor(baseColor);

        if (patterns != null) {
            patterns.forEach(item::addPattern);
        }

        if (enchants != null) {
            enchants.forEach(item::addEnchant);
        }

        if (flags != null) {
            flags.forEach(item::addFlag);
        }

        return item;
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return this;
    }
}
