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

import java.util.ArrayList;
import java.util.List;

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

        // Create the array with spaces for the two borders
        // The minimum size the array can have is 9, since we need to add the borders even on small inventories
        List<ItemStack> result = new ArrayList<>(Math.max(9, size + (rows * 2)));

        addBorders(result, rows, true);
        addBorders(result, rows, false);

        for (int i = 0; i < size; i++) {
            result.add(items.get(i));
        }

        return result;
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

    private void addBorders(List<ItemStack> result, int rows, boolean left) {
        int placed = 0;
        int slot = left ? 0 : 8;

        while (placed <= rows) {
            slot += 9;
            placed++;

            boolean last = placed == rows;

            if (last) {
                result.add(slot, left ? next().build() : previous().build());
                break;
            }

            result.add(slot, EMPTY.build());
        }
    }

    @Override
    public int getUsableSpace() {
        return 5 * PER_ROW;
    }
}
