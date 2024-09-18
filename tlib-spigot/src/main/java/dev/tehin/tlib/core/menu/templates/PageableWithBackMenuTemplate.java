package dev.tehin.tlib.core.menu.templates;

import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.core.menu.MenuContentBuilder;
import dev.tehin.tlib.core.menu.MenuFilter;
import dev.tehin.tlib.utilities.item.ItemDefaults;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PageableWithBackMenuTemplate extends PageableMenuTemplate {

    private final Menu back;

    public PageableWithBackMenuTemplate(Menu menu, MenuFilter filter, int currentPage, Menu back) {
        super(menu, filter, currentPage);
        this.back = back;
    }

    public PageableWithBackMenuTemplate(Menu menu, MenuFilter filter, int currentPage, Class<? extends Menu> back) {
        super(menu, filter, currentPage);
        this.back = tLib.get().getMenu().getMenu(back);
    }

    @Override
    protected void addOptions(List<ItemStack> items, int itemCount, MenuContentBuilder builder) {
        items.add(builder.register(ItemDefaults.BACK(back)));
        addEmpty(items, 1);
        items.add(previous(builder));
        addEmpty(items, 1);
        items.add(builder.getPresets().getFilter(getFilter()));
        addEmpty(items, 1);
        items.add(next(itemCount, builder));
        addEmpty(items, 2);
    }
}
