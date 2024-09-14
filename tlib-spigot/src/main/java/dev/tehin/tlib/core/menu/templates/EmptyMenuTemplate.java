package dev.tehin.tlib.core.menu.templates;

import dev.tehin.tlib.core.menu.MenuTemplate;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EmptyMenuTemplate implements MenuTemplate {

    @Override
    public List<ItemStack> apply(List<ItemStack> items) {
        while (items.size() % 9 != 0) {
            items.add(null);
        }

        return items;
    }

    @Override
    public int getMaxRows() {
        return 6;
    }

    @Override
    public int getMaxColumns() {
        return 9;
    }

}
