package dev.tehin.tlib.core.menu;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.action.data.ItemData;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.utilities.item.ItemUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MenuContentBuilder {

    private final Menu menu;
    private final List<ItemStack> contents = new ArrayList<>(36);

    public MenuContentBuilder add(ItemBuilder builder) {
        ItemStack stack = generate(builder);

        this.contents.add(stack);
        return this;
    }

    public MenuContentBuilder set(int index, ItemBuilder builder) {
        ItemStack stack = generate(builder);

        this.contents.set(index, stack);
        return this;
    }

    public List<ItemStack> build() {
        return this.contents;
    }

    private ItemStack generate(ItemBuilder builder) {
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

        return ItemUtil.addTag(item, "action", String.valueOf(id));
    }
}
