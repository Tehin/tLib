package dev.tehin.tlib.core.menu.templates;

import dev.tehin.tlib.api.menu.action.ActionExecutor;
import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.core.menu.MenuActions;
import dev.tehin.tlib.core.menu.MenuTemplate;
import dev.tehin.tlib.core.menu.action.ErrorAction;
import dev.tehin.tlib.core.menu.action.ExecutorAction;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@RequiredArgsConstructor
public class PageableMenuTemplate implements MenuTemplate {

    private static final int PER_ROW = 7;
    private static final ItemBuilder EMPTY = new ItemBuilder(Material.STAINED_GLASS_PANE).name("&7").data(0);

    private final Menu menu;
    private final int currentPage;
    private final int maxPage;

    @Override
    public List<ItemStack> apply(List<ItemStack> items) {
        int size = items.size();

        int maxItemsThisPage = getUsableSpace() * (currentPage + 1);

        // We subtract 1 if not on the first page since we are getting the index inclusive
        int firstItemThisPage = (currentPage == 0) ? 0 : (getUsableSpace() * currentPage) - 1;

        // If we are not on the first page, or in the first page but, we have more items than we can use, trim the list for our needs
        if (size > maxItemsThisPage || currentPage != 0) {
            items = items.subList(firstItemThisPage, Math.min(size, maxItemsThisPage));
        }

        while (items.size() % PER_ROW != 0) {
            items.add(null);
            size++;
        }

        int rows = size / PER_ROW;
        System.out.println("Rows: " + rows);

        int max = Math.max(9, size + (rows * 2));
        System.out.println("Max: " + max);

        ItemStack[] result = new ItemStack[max];

        addBorders(result, rows, true);
        addBorders(result, rows, false);

        Iterator<ItemStack> iterator = items.iterator();
        for (int i = 0; i < max; i++) {
            if (!iterator.hasNext()) break;

            ItemStack current = result[i];

            // Fill every empty space with an item
            if (current == null) result[i] = iterator.next();
        }

        // Fill the items with the correct ones since for whatever reason we cannot create a list based on nullified arrays
        items.clear();
        for (int i = 0; i < max; i++) {
            items.add(result[i]);
        }

        return items;
    }

    private ItemBuilder previous() {
        String color = "&a";
        int data = 13;
        MenuAction action = new ExecutorAction(player -> menu.open(player, currentPage - 1));

        if (currentPage == 0) {
            color = "&c";
            data = 14;
            action = new ErrorAction();
        }

        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .data(data)
                .name(color + "&lPágina Anterior")
                .action(action)
                .amount(currentPage - 1);
    }

    private ItemBuilder next() {
        String color = "&a";
        int data = 13;
        MenuAction action = new ExecutorAction(player -> menu.open(player, currentPage + 1));

        if (currentPage == maxPage) {
            color = "&c";
            data = 14;
            action = new ErrorAction();
        }

        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .data(data)
                .name(color + "&lPágina Siguiente")
                .action(action)
                .amount(currentPage - 1);
    }

    private void addBorders(ItemStack[] result, int rows, boolean left) {
        int placed = 0;
        int slot = left ? 1 : 9;

        while (placed < rows) {
            boolean last = placed == rows - 1;

            ItemStack stack;
            if (last) {
                System.out.println("Border Action: " + (slot - 1));
                stack = left ? previous().build() : next().build();
            } else {
                System.out.println("Border Empty: " + (slot - 1));
                stack = EMPTY.build();
            }

            // Use -1 since our index is one lower than our +9 sum
            result[slot - 1] = stack;

            slot += 9;
            placed++;
        }
    }

    @Override
    public int getUsableSpace() {
        return 5 * PER_ROW;
    }
}
