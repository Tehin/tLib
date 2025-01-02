package dev.tehin.tlib.utilities.inventory;

import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.MenuContentBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryCenterer {

    private static final CenteredLine[][] centeredInventories = new CenteredLine[][] {
            {CenteredLine.ONE},
            {CenteredLine.TWO},
            {CenteredLine.THREE_BIG},
            {CenteredLine.FOUR},
            {CenteredLine.THREE_BIG, CenteredLine.TWO},
            {CenteredLine.THREE_BIG, CenteredLine.EMPTY, CenteredLine.THREE_BIG},
            {CenteredLine.FOUR, CenteredLine.EMPTY, CenteredLine.THREE},
            {CenteredLine.FOUR, CenteredLine.EMPTY, CenteredLine.FOUR},
            {CenteredLine.FIVE, CenteredLine.EMPTY, CenteredLine.FOUR},
            {CenteredLine.FIVE, CenteredLine.EMPTY, CenteredLine.FIVE},
            {CenteredLine.FOUR, CenteredLine.THREE, CenteredLine.FOUR},
            {CenteredLine.FIVE, CenteredLine.FOUR, CenteredLine.THREE},
            {CenteredLine.FOUR, CenteredLine.FIVE, CenteredLine.FOUR},
            {CenteredLine.FIVE, CenteredLine.FOUR, CenteredLine.FIVE},
            {CenteredLine.FOUR, CenteredLine.FIVE, CenteredLine.FOUR, CenteredLine.TWO},
            {CenteredLine.FOUR, CenteredLine.FIVE, CenteredLine.FOUR, CenteredLine.THREE},
            {CenteredLine.FOUR, CenteredLine.FIVE, CenteredLine.FOUR, CenteredLine.FOUR_UGLY},
            {CenteredLine.FOUR, CenteredLine.FIVE, CenteredLine.FOUR, CenteredLine.FIVE}
    };

    @Deprecated(forRemoval = true)
    public static <T extends ItemBuilderProvider> Map<T, Integer> centerWithProvider(MenuContentBuilder content, List<T> items) {
        if (items.size() > 18 || items.isEmpty()) return null;

        final Map<T, Integer> result = new HashMap<>();

        CenteredLine[] centeredLines = centeredInventories[items.size() - 1];

        int count = 0;
        for(CenteredLine line : centeredLines) {
            for(boolean value : line.getLine()) {
                if(value) {
                    T item = items.get(count);

                    // Add to result before adding to content so we get the exact slot
                    result.put(item, content.size());

                    content.add(item.getItemBuilder());
                    count++;
                } else {
                    content.add(null);
                }
            }
        }

        return result;
    }

    @Deprecated(forRemoval = true)
    public static void centerWithBuilder(MenuContentBuilder content, List<ItemBuilder> items) {
        if (items.size() > 18 || items.isEmpty()) return;

        CenteredLine[] centeredLines = centeredInventories[items.size() - 1];

        int count = 0;
        for(CenteredLine line : centeredLines) {
            for(boolean value : line.getLine()) {
                if(value) {
                    ItemBuilder item = items.get(count);
                    content.add(item);
                    count++;
                } else {
                    content.add(null);
                }
            }
        }
    }

    @Deprecated(forRemoval = true)
    public static void centerWithBuilder(MenuContentBuilder content, ItemBuilder... items) {
        if (items.length > 18 || items.length == 0) return;

        CenteredLine[] centeredLines = centeredInventories[items.length - 1];

        int count = 0;
        for(CenteredLine line : centeredLines) {
            for(boolean value : line.getLine()) {
                if(value) {
                    ItemBuilder item = items[count];
                    content.add(item);
                    count++;
                } else {
                    content.add(null);
                }
            }
        }
    }

    private enum CenteredLine {
        EMPTY(false,false,false,false,false,false,false,false,false),
        ONE(false,false,false,false,true,false,false,false,false),
        TWO(false,false,true,false,false,false,true,false,false),
        THREE(false,false,true,false,true,false,true,false,false),
        THREE_BIG(false,true,false,false,true,false,false,true,false),
        FOUR(false,true,false,true,false,true,false,true,false),
        FOUR_UGLY(true,false,true,false,false,false,true,false,true),
        FIVE(true,false,true,false,true,false,true,false,true);

        private boolean[] line;

        private boolean[] getLine() {
            return line;
        }

        private CenteredLine(boolean... line) {
            this.line = line;
        }
    }

    public static void center(MenuContentBuilder content, ItemBuilderProvider... items) {
        if (items.length > 18 || items.length == 0) return;

        CenteredLine[] centeredLines = centeredInventories[items.length - 1];

        int count = 0;
        for(CenteredLine line : centeredLines) {
            for(boolean value : line.getLine()) {
                if(value) {
                    ItemBuilder item = items[count].getItemBuilder();
                    content.add(item);
                    count++;
                } else {
                    content.add(null);
                }
            }
        }
    }

    public static <T extends ItemBuilderProvider> Map<T, Integer> center(MenuContentBuilder content, List<T> items) {
        if (items.size() > 18 || items.isEmpty()) return null;

        final Map<T, Integer> result = new HashMap<>();

        CenteredLine[] centeredLines = centeredInventories[items.size() - 1];

        int count = 0;
        for(CenteredLine line : centeredLines) {
            for(boolean value : line.getLine()) {
                if(value) {
                    T item = items.get(count);

                    // Add to result before adding to content so we get the exact slot
                    result.put(item, content.size());

                    content.add(item.getItemBuilder());
                    count++;
                } else {
                    content.add(null);
                }
            }
        }

        return result;
    }

}
