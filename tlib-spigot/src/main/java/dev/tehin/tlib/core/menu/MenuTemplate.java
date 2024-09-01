package dev.tehin.tlib.core.menu;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface MenuTemplate {
    List<ItemStack> apply(List<ItemStack> items);

    int getMaxRows();
    int getMaxColumns();
}
