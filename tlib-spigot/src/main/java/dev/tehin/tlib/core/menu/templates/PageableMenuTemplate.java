package dev.tehin.tlib.core.menu.templates;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.core.menu.MenuContentBuilder;
import dev.tehin.tlib.core.menu.MenuTemplate;
import dev.tehin.tlib.core.menu.action.ExecutorAction;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@RequiredArgsConstructor
public class PageableMenuTemplate implements MenuTemplate {

    private static final ItemBuilder PANE = new ItemBuilder(Material.STAINED_GLASS_PANE).name("&7").data(0);

    private final Menu menu;
    private final int currentPage;
    private final int maxPage;

    @Override
    public List<ItemStack> apply(List<ItemStack> items) {
        // Use builder to register actions
        MenuContentBuilder builder = new MenuContentBuilder(menu);

        final int maxContent = getMaxColumns() * getMaxRows();
        final boolean firstPage = currentPage == 0;

        boolean isFull = items.size() >= maxContent;

        int start = Math.max(0, (9 * currentPage) - 1);

        // If first page, get the max content minus one since we start from 0 and
        // not from our desired page start
        int end = (firstPage) ? maxContent - 1 : start + (9 * maxContent);

        // We add one since end is exclusive
        items = items.subList(start, Math.min(items.size(), end + 1));
        if (!isFull) fill(items);

        addSeparator(items);
        addOptions(items, builder);

        return items;
    }

    private void fill(List<ItemStack> items) {
        while (items.size() % 9 != 0) {
            items.add(null);
        }
    }

    private void addEmpty(List<ItemStack> items, int quantity) {
        for (int i = 0; i < quantity; i++) {
            items.add(null);
        }
    }

    private ItemStack previous(MenuContentBuilder builder) {
        if (currentPage == 0) return new ItemStack(Material.AIR);

        MenuAction action = new ExecutorAction(player -> {
            player.closeInventory();
            menu.open(player, currentPage + 1);
        });

        // Parse since page starts from 0 and not from 1
        final int previousPageParsed = currentPage;

        ItemBuilder item = new ItemBuilder(Material.STAINED_GLASS_PANE)
                .data(13)
                .name("&a&lAnterior &7(Página #" + previousPageParsed + ")")
                .action(action)
                .amount(previousPageParsed);

        return builder.register(item);
    }

    private ItemStack next(MenuContentBuilder builder) {
        if (currentPage == maxPage) return new ItemStack(Material.AIR);

        MenuAction action = new ExecutorAction(player -> {
            player.closeInventory();
            menu.open(player, currentPage + 1);
        });

        // Parse since page starts from 0 and not from 1
        final int nextPageParsed = currentPage + 2;
        ItemBuilder item = new ItemBuilder(Material.STAINED_GLASS_PANE)
                .data(13)
                .name("&a&lSiguiente &7(Página #" + nextPageParsed + ")")
                .action(action)
                .amount(nextPageParsed);

        return builder.register(item);
    }

    private ItemBuilder filter() {
        return new ItemBuilder(Material.HOPPER)
                .name("&f&lFiltrar");
    }

    private void addSeparator(List<ItemStack> items) {
        ItemStack stack = PANE.build();

        for (int i = 0; i < 9; i++) {
            items.add(stack);
        }
    }

    private void addOptions(List<ItemStack> items, MenuContentBuilder builder) {
        items.add(previous(builder));
        addEmpty(items, 3);
        items.add(filter().build());
        addEmpty(items, 3);
        items.add(next(builder));
    }

    @Override
    public int getMaxRows() {
        return 3;
    }

    @Override
    public int getMaxColumns() {
        return 9;
    }
}
