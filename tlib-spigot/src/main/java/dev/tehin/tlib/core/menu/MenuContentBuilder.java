package dev.tehin.tlib.core.menu;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.action.data.ItemData;
import dev.tehin.tlib.api.menu.features.PageableMenu;
import dev.tehin.tlib.api.menu.features.StaticMenu;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.utilities.inventory.InventoryCenterer;
import dev.tehin.tlib.utilities.inventory.ItemBuilderProvider;
import dev.tehin.tlib.utilities.item.ItemUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MenuContentBuilder {

    private static final int DEFAULT_PAGE_SIZE = 5 * 9;
    private static final int PAGEABLE_INVENTORY_SIZE = 5 * 7;

    private final Menu menu;
    private final List<ItemStack> contents = new ArrayList<>(54);

    private MenuPresets presets;

    private Player player;

    // We can set the player so all the items are parsed correctly if needed
    public void setPlayer(Player player) {
        if (menu instanceof StaticMenu) throw new IllegalStateException("Cannot set the player for a static menu");

        this.player = player;
    }

    public MenuPresets getPresets() {
        if (presets == null) presets = new MenuPresets(menu);

        return presets;
    }

    public MenuContentBuilder clear() {
        this.contents.clear();
        return this;
    }

    public List<ItemStack> getRawItems() {
        return this.contents;
    }

    public MenuContentBuilder set(int index, ItemBuilder builder) {
        ItemStack stack = register(builder, this.player);

        if (contents.size() > index) {
            contents.set(index, stack);
        } else {
            while (contents.size() < index) {
                contents.add(null);
            }

            contents.add(stack);
        }

        return this;
    }

    public int size() {
        return contents.size();
    }

    public List<ItemStack> build(MenuTemplate template) {
        boolean isPageable = menu instanceof PageableMenu;

        int currentItems = contents.size();

        // How many items can we freely use
        final int maxItems = template.getMaxRows() * template.getMaxColumns();

        // If we have more items than our usable space, prevent menu creation
        boolean overflowed = currentItems > maxItems;
        if (overflowed && !isPageable) throw new IllegalStateException("Menu overflowed, please implement PageableMenu");

        // Apply the template to the item list
        template.apply(this);

        return contents;
    }

    public ItemStack register(ItemBuilder builder, Player player) {
        // All the menus that have the player set are designed to
        // parse lang, so just do it
        if (this.player != null) {
            builder.applyLang(true);
        }

        ItemStack item = builder.build(player);
        MenuAction action = builder.getAction();

        if (action == null) return item;

        /*
         * If the action is not null, and we haven't already defined the item action, we do not execute
         */
        int id = menu.getActions().getNextId();

        /*
         * We first set the action data, so we can compare it with the already cached ones
         * The ID is set later since it is defined by our cache
         *
         * We use the data of the ItemBuilder itself since some items may have lang,
         * and so we have to get the raw message, not the parsed one
         */
        ItemData data = ItemData.of(builder);
        action.setData(data);

        Optional<MenuAction> cache = menu.getActions().get(data);
        if (cache.isEmpty()) {
            action.setId(id);
            menu.getActions().set(id, action);
        } else {
            id = cache.get().getId();
        }

        return ItemUtil.addTag(item, "menu-action", String.valueOf(id));
    }

    public MenuContentBuilder add(ItemBuilder... builders) {
        if (builders == null) {
            this.contents.add(null);
            return this;
        }

        for (ItemBuilder builder : builders) {
            if (builder == null) {
                this.contents.add(null);
                continue;
            }

            ItemStack stack = register(builder, this.player);

            this.contents.add(stack);
        }

        return this;
    }

    public MenuContentBuilder addRaw(ItemStack item) {
        this.contents.add(item);
        return this;
    }

    public MenuContentBuilder setRaw(int index, ItemStack item) {
        if (size() < index) {
            while (size() < index) {
                this.contents.add(null);
            }

            this.contents.add(item);
        } else {
            this.contents.set(index, item);
        }

        return this;
    }

    public void addAll(List<ItemStack> newItems) {
        this.contents.addAll(newItems);
    }

    public void addEmpty(int quantity) {
        for (int i = 0; i < quantity; i++) {
            add(null);
        }
    }

    public void fill() {
        while (size() % 9 != 0) {
            add(null);
        }
    }

    public void fill(ItemBuilder filler) {
        while (size() % 9 != 0) {
            add(filler);
        }
    }

    public void addRow(ItemBuilder filler) {
        for (int i = 0; i < 9; i++) {
            add(filler);
        }
    }

    public void addCentered(ItemBuilderProvider... items) {
        InventoryCenterer.center(this, items);
    }

    public <T extends ItemBuilderProvider> void addCentered(List<T> items) {
        InventoryCenterer.center(this, items);
    }

}
