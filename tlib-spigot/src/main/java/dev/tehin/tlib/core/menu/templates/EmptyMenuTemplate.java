package dev.tehin.tlib.core.menu.templates;

import dev.tehin.tlib.core.menu.MenuContentBuilder;
import dev.tehin.tlib.core.menu.MenuTemplate;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EmptyMenuTemplate implements MenuTemplate {

    @Override
    public void apply(MenuContentBuilder items) {
        while (items.size() % 9 != 0) {
            items.add(null);
        }
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
