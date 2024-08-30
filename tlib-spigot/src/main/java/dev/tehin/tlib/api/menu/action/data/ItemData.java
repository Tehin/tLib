package dev.tehin.tlib.api.menu.action.data;

import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.utilities.MessageUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public record ItemData(String name, List<String> lore) {

    public static ItemData of(ItemBuilder builder) {
        List<String> lore = builder.getLore().stream()
                .map(MessageUtil::color)
                .collect(Collectors.toList());

        return new ItemData(builder.getName(), lore);
    }
}