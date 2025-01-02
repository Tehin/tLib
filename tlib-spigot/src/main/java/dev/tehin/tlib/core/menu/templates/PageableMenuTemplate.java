package dev.tehin.tlib.core.menu.templates;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.core.menu.MenuContentBuilder;
import dev.tehin.tlib.core.menu.MenuTemplate;
import dev.tehin.tlib.core.menu.action.ExecutorAction;
import dev.tehin.tlib.core.menu.MenuFilter;
import dev.tehin.tlib.utilities.PaginationUtil;
import dev.tehin.tlib.utilities.item.ItemDefaults;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minemora.nms.NMS;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
public class PageableMenuTemplate implements MenuTemplate {

    private final Menu menu;
    private final MenuFilter filter;
    private final int currentPage;

    @Override
    public void apply(MenuContentBuilder content) {
        int itemCount = content.size();

        final boolean firstPage = currentPage == 0;

        List<ItemStack> newItems = paginate(content.getRawItems());
        boolean isFull = newItems.size() >= getMaxContent();

        content.clear();
        content.addAll(newItems);

        // Fill items if not full, try to adjust to the size of items if it's the first and only page
        // If not, fill the whole inventory so the menu does not change suddenly of size
        if (!isFull) fill(content, !firstPage);

        addSeparator(content);
        addOptions(content, itemCount);
    }

    protected void fill(MenuContentBuilder content, boolean full) {
        if (!full) {
            while (content.size() % 9 != 0) {
                content.add(null);
            }
        } else {
            while (content.size() < getMaxRows() * getMaxColumns()) {
                content.add(null);
            }
        }
    }

    protected ItemBuilder previous() {
        if (currentPage == 0) return null;

        MenuAction action = new ExecutorAction(player -> {
            menu.open(player, currentPage - 1, filter);
        });

        // Parse since page starts from 0 and not from 1
        final int previousPageParsed = currentPage;

        return new ItemBuilder(NMS.Material.BANNER)
                .baseColor(DyeColor.WHITE)
                .addPattern(new Pattern(DyeColor.RED, PatternType.RHOMBUS_MIDDLE))
                .addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT))
                .addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_RIGHT))
                .addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_RIGHT))
                .name("&a&lAnterior &7(Página #" + previousPageParsed + ")")
                .action(action)
                .amount(previousPageParsed);
    }

    protected ItemBuilder next(int itemCount) {
        int pages = (int) Math.ceil((double) itemCount / getMaxContent());

        if (currentPage == (pages - 1) || pages == 1) return null;

        MenuAction action = new ExecutorAction(player -> {
            menu.open(player, currentPage + 1, filter);
        });

        // Parse since page starts from 0 and not from 1
        final int nextPageParsed = currentPage + 2;

        return new ItemBuilder(NMS.Material.BANNER)
                .baseColor(DyeColor.WHITE)
                .addPattern(new Pattern(DyeColor.GREEN, PatternType.RHOMBUS_MIDDLE))
                .addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT))
                .addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT))
                .addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_LEFT))
                .name("&a&lSiguiente &7(Página #" + nextPageParsed + ")")
                .action(action)
                .amount(nextPageParsed);
    }

    protected void addSeparator(MenuContentBuilder content) {
        for (int i = 0; i < 9; i++) {
            content.add(ItemDefaults.GLASS);
        }
    }

    protected void addOptions(MenuContentBuilder content, int itemCount) {
        content.add(previous());
        content.addEmpty(3);
        content.add(content.getPresets().getFilter(filter));
        content.addEmpty(3);
        content.add(next(itemCount));
    }

    protected List<ItemStack> paginate(List<ItemStack> items) {
        return PaginationUtil.paginate(items, currentPage, getMaxContent());
    }

    @Override
    public int getMaxRows() {
        return 3;
    }

    @Override
    public int getMaxColumns() {
        return 9;
    }

    protected int getMaxContent() {
        return getMaxRows() * getMaxColumns();
    }
}
