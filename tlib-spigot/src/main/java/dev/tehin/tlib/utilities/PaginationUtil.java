package dev.tehin.tlib.utilities;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PaginationUtil {

    public static List<ItemStack> paginate(List<ItemStack> items, int currentPage, int pageSize) {
        final boolean firstPage = currentPage == 0;

        int start = Math.max(0, (pageSize * currentPage) - 1);

        // If first page, get the max content minus one since we start from 0 and
        // not from our desired page start.
        int end = (firstPage) ? pageSize : start + pageSize;

        // We add one since end is exclusive
        return items.subList(start, Math.min(items.size(), end));
    }

}
