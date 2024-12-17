package dev.tehin.tlib.core.menu.templates;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.core.menu.MenuContentBuilder;
import dev.tehin.tlib.core.menu.MenuTemplate;
import dev.tehin.tlib.core.menu.action.ExecutorAction;
import dev.tehin.tlib.core.menu.MenuFilter;
import dev.tehin.tlib.utilities.PaginationUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minemora.nms.NMS;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
public class PageableMenuTemplate implements MenuTemplate {

    private static final ItemBuilder PANE = new ItemBuilder(NMS.Material.STAINED_GLASS_PANE).name("&7").data(0);

    private final Menu menu;
    private final MenuFilter filter;
    private final int currentPage;

    @Override
    public List<ItemStack> apply(List<ItemStack> items) {
        // Use builder to register actions
        MenuContentBuilder builder = new MenuContentBuilder(menu);
        int itemCount = items.size();

        final boolean firstPage = currentPage == 0;

        items = paginate(items);
        boolean isFull = items.size() >= getMaxContent();

        // Fill items if not full, try to adjust to the size of items if it's the first and only page
        // If not, fill the whole inventory so the menu does not change suddenly of size
        if (!isFull) fill(items, !firstPage);

        addSeparator(items);
        addOptions(items, itemCount, builder);

        return items;
    }

    protected void fill(List<ItemStack> items, boolean full) {
        if (!full) {
            while (items.size() % 9 != 0) {
                items.add(null);
            }
        } else {
            while (items.size() < getMaxRows() * getMaxColumns()) {
                items.add(null);
            }
        }
    }

    protected void addEmpty(List<ItemStack> items, int quantity) {
        for (int i = 0; i < quantity; i++) {
            items.add(null);
        }
    }

    protected ItemStack previous(MenuContentBuilder builder) {
        if (currentPage == 0) return new ItemStack(NMS.Material.AIR);

        MenuAction action = new ExecutorAction(player -> {
            menu.open(player, currentPage - 1, filter);
        });

        // Parse since page starts from 0 and not from 1
        final int previousPageParsed = currentPage;

        ItemBuilder item = new ItemBuilder(NMS.Material.BANNER)
                .baseColor(DyeColor.WHITE)
                .addPattern(new Pattern(DyeColor.RED, PatternType.RHOMBUS_MIDDLE))
                .addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT))
                .addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_RIGHT))
                .addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_RIGHT))
                .name("&a&lAnterior &7(Página #" + previousPageParsed + ")")
                .action(action)
                .amount(previousPageParsed);

        return builder.register(item);
    }

    protected ItemStack next(int itemCount, MenuContentBuilder builder) {
        int pages = (int) Math.ceil((double) itemCount / getMaxContent());

        if (currentPage == (pages - 1) || pages == 1) return new ItemStack(NMS.Material.AIR);

        MenuAction action = new ExecutorAction(player -> {
            menu.open(player, currentPage + 1, filter);
        });

        // Parse since page starts from 0 and not from 1
        final int nextPageParsed = currentPage + 2;
        ItemBuilder item = new ItemBuilder(NMS.Material.BANNER)
                .baseColor(DyeColor.WHITE)
                .addPattern(new Pattern(DyeColor.GREEN, PatternType.RHOMBUS_MIDDLE))
                .addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT))
                .addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT))
                .addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_LEFT))
                .name("&a&lSiguiente &7(Página #" + nextPageParsed + ")")
                .action(action)
                .amount(nextPageParsed);

        return builder.register(item);
    }

    protected void addSeparator(List<ItemStack> items) {
        ItemStack stack = PANE.build();

        for (int i = 0; i < 9; i++) {
            items.add(stack);
        }
    }

    protected void addOptions(List<ItemStack> items, int itemCount, MenuContentBuilder builder) {
        items.add(previous(builder));
        addEmpty(items, 3);
        items.add(builder.getPresets().getFilter(filter));
        addEmpty(items, 3);
        items.add(next(itemCount, builder));
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
