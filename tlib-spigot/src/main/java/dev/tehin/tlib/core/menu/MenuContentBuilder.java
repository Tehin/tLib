package dev.tehin.tlib.core.menu;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.action.data.ItemData;
import dev.tehin.tlib.api.menu.features.PageableMenu;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.templates.EmptyMenuTemplate;
import dev.tehin.tlib.core.menu.templates.PageableMenuTemplate;
import dev.tehin.tlib.utilities.item.ItemUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

    public MenuPresets getPresets() {
        if (presets == null) presets = new MenuPresets(menu);

        return presets;
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

            ItemStack stack = register(builder);

            this.contents.add(stack);
        }

        return this;
    }

    public MenuContentBuilder set(int index, ItemBuilder builder) {
        ItemStack stack = register(builder);

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

    public List<ItemStack> build(MenuTemplate template, boolean useTemplate) {
        if (!useTemplate) return contents;

        boolean isPageable = menu instanceof PageableMenu;

        int currentItems = contents.size();

        // How many items can we freely use
        final int maxItems = template.getMaxRows() * template.getMaxColumns();

        // If we have more items than our usable space, prevent menu creation
        boolean overflowed = currentItems > maxItems;
        if (overflowed && !isPageable) throw new IllegalStateException("Menu overflowed, please implement PageableMenu");

        // Apply the template to the item list
        return template.apply(contents);
    }

    public ItemStack register(ItemBuilder builder) {
        ItemStack item = builder.build();
        MenuAction action = builder.getAction();

        if (action == null) return item;

        /*
         * If the action is not null, and we haven't already defined the item action, we do not execute
         */
        int id = menu.getActions().getNextId();

        /*
         * We first set the action data, so we can compare it with the already cached ones
         * The ID is set later since it is defined by our cache
         */
        ItemData data = ItemData.of(item);
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
}
