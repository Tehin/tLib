package dev.tehin.tlib.core.menu.templates;

import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.core.menu.MenuContentBuilder;
import dev.tehin.tlib.core.menu.MenuFilter;
import dev.tehin.tlib.utilities.item.ItemDefaults;

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
    protected void addOptions(MenuContentBuilder content, int itemCount) {
        content.add(ItemDefaults.BACK(back));
        content.addEmpty(1);
        content.add(previous());
        content.addEmpty(1);
        content.add(content.getPresets().getFilter(getFilter()));
        content.addEmpty(1);
        content.add(next(itemCount, getFilter()));
        content.addEmpty(2);
    }

}
