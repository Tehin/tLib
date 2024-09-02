package dev.tehin.tlib.core.menu;

import dev.tehin.tlib.api.menu.action.ActionExecutor;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.action.ExecutorAction;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MenuPresets {

    private final Menu menu;

    public ItemStack getFilter(MenuFilter current) {
        List<String> lore = new ArrayList<>();

        MenuFilter next = MenuFilter.ALL;

        int index = 0;
        MenuFilter[] values = MenuFilter.values();
        for (MenuFilter value : values) {
            if (value == current) {
                lore.add("&a▶ &f" + value.getDisplay());

                next = MenuFilter.values()[index + 1 == values.length ? 0 : index + 1];
            } else {
                lore.add("&f▶ &7" + value.getDisplay());
            }

            index++;
        }

        lore.add("");
        lore.add("&7Clic para ver &f" + next.getDisplay());

        MenuFilter finalNext = next;
        ActionExecutor action = player -> menu.open(player, 0, finalNext);

        ItemBuilder builder = new ItemBuilder(Material.HOPPER)
                .name("&f&lFiltrar")
                .lore(lore)
                .glow(current != MenuFilter.ALL)
                .action(new ExecutorAction(action));

        return menu.createContentBuilder().register(builder);
    }

}
