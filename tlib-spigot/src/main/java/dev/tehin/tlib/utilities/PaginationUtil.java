package dev.tehin.tlib.utilities;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtil {

    public static List<ItemStack> paginate(List<ItemStack> items, int currentPage, int pageSize) {
        int start = Math.max(0, (pageSize * currentPage) - 1);
        int end = start + pageSize;

        return new ArrayList<>(items.subList(start, Math.min(items.size(), end)));
    }

}
