package dev.tehin.tlib.core.menu;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface MenuTemplate {

    void apply(MenuContentBuilder content);

    int getMaxRows();
    int getMaxColumns();
}
