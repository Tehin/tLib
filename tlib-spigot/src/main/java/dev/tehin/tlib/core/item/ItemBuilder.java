package dev.tehin.tlib.core.item;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.core.lang.LangParser;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;
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
    private boolean applyLang = false;

    @Setter(AccessLevel.NONE)
    private List<ItemFlag> flags = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    private Map<Enchantment, Integer> enchants = null;

    @Setter(AccessLevel.NONE)
    private List<Pattern> patterns = null;

    private DyeColor baseColor = null;

    public ItemStack build() {
        return build(null);
    }

    public ItemStack build(Player player) {
        ItemStack base = NMS.get().getItemStackCreator().create(material).getItemStack();
        base.setAmount(amount);

        setProperties(base, player);

        return base;
    }

    public void apply(ItemStack found) {
        setProperties(found, null);
    }

    public void apply(ItemStack found, Player player) {
        setProperties(found, player);
    }

    private void setProperties(ItemStack item, Player player) {
        NMS.get().getUtil().setItemStackData(item, (short) data);

        ItemMeta meta = item.getItemMeta();

        if (enchants != null) {
            // If we are adding enchants to an enchanted book, we need to use the EnchantmentStorageMeta
            if (meta instanceof EnchantmentStorageMeta enchantMeta) {
                for (Map.Entry<Enchantment, Integer> enchantment : enchants.entrySet()) {
                    enchantMeta.addStoredEnchant(enchantment.getKey(), enchantment.getValue(), true);
                }
            } else {
                for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                    meta.addEnchant(entry.getKey(), entry.getValue(), true);
                }
            }
        }

        if (meta instanceof BannerMeta bannerMeta) {
            if (baseColor != null) NMS.get().getItemStackCreator().fromStack(item).setBaseColor(baseColor);

            if (patterns != null) patterns.forEach(bannerMeta::addPattern);
        }

        if (name != null) {
            String itemName = MessageUtil.color(name);
            if (applyLang && player != null) itemName = LangParser.parse(player, itemName);

            meta.setDisplayName(itemName);
        }

        if (!lore.isEmpty()) {
            List<String> itemLore = new ArrayList<>();

            for (String loreLine : lore) {
                if (applyLang && player != null) {
                    itemLore.add(LangParser.parse(player, MessageUtil.color(loreLine)));
                } else {
                    itemLore.add(MessageUtil.color(loreLine));
                }
            }

            meta.setLore(itemLore);
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

    public Map<Enchantment, Integer> getEnchants() {
        return enchants;
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

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(material.name());

        if (name != null) {
            string.append(" : ").append("name=").append(name);
        }

        if (data != 0) {
            string.append(" : ").append("data=").append(data);
        }

        if (amount != 1) {
            string.append(" : ").append("amount=").append(amount);
        }

        if (color != null) {
            string.append(" : ").append("color=").append(color.name());
        }

        if (glow) {
            string.append(" : ").append("glow=").append(true);
        }

        if (enchants != null && !enchants.isEmpty()) {
            enchants.forEach((enchantment, level) -> {
                string.append(" : ").append("enchant=").append(enchantment.getName()).append("=").append(level);
            });
        }

        for (String s : lore) {
            string.append(" : ").append("lore-line=").append(s);
        }

        for (ItemFlag flag : flags) {
            string.append(" : ").append("flag=").append(flag.name());
        }

        return string.toString();
    }

    public static ItemBuilder fromString(String serialized) {
        String[] split = serialized.split(" : ");
        ItemBuilder builder = new ItemBuilder(Material.valueOf(split[0]));

        for (int i = 1; i < split.length; i++) {
            String[] argSplit = split[i].split("=");
            switch (argSplit[0]) {
                case "name":
                    builder.name(argSplit[1]);
                    break;
                case "data":
                    builder.data(Integer.parseInt(argSplit[1]));
                    break;
                case "amount":
                    builder.amount(Integer.parseInt(argSplit[1]));
                    break;
                case "color":
                    builder.color(DyeColor.valueOf(argSplit[1]));
                    break;
                case "glow":
                    builder.glow(Boolean.parseBoolean(argSplit[1]));
                    break;
                case "enchant":
                    builder.addEnchant(Enchantment.getByName(argSplit[1]), Integer.parseInt(argSplit[2]));
                    break;
                case "lore-line":
                    builder.lore(argSplit[1]);
                    break;
                case "flag":
                    builder.addFlag(ItemFlag.valueOf(argSplit[1]));
                    break;
            }
        }

        return builder;
    }

    public static ItemBuilder fromStack(ItemStack stack) {
        ItemBuilder builder = new ItemBuilder(stack.getType())
                .amount(stack.getAmount())
                .data(stack.getDurability());

        if (!stack.hasItemMeta()) return builder;

        ItemMeta meta = stack.getItemMeta();

        if (meta.hasDisplayName()) {
            builder.name(meta.getDisplayName());
        }

        if (meta.hasLore()) {
            builder.lore(meta.getLore());
        }

        if (meta.hasEnchants()) {
            meta.getEnchants().forEach(builder::addEnchant);
        }

        meta.getItemFlags().forEach(builder::addFlag);

        if (meta instanceof LeatherArmorMeta leatherMeta) {
            builder.color(DyeColor.getByColor(leatherMeta.getColor()));
        }

        if (meta instanceof BannerMeta bannerMeta) {
            builder.baseColor(bannerMeta.getBaseColor());

            for (Pattern pattern : bannerMeta.getPatterns()) {
                builder.addPattern(pattern);
            }
        }

        if (meta.hasLore()) {
            builder.lore(meta.getLore());
        }

        return builder;
    }

}
