package dev.tehin.tlib.core.menu.craft;

import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.api.menu.MenuType;
import dev.tehin.tlib.api.menu.action.data.ActionData;
import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.craft.ItemProvider;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.action.CraftMenuAction;
import dev.tehin.tlib.core.menu.action.CraftNavigationAction;
import dev.tehin.tlib.utilities.item.ItemUtils;
import dev.tehin.tlib.utilities.task.TaskUtil;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;
import java.util.function.Consumer;

@AllArgsConstructor
public class CraftItemProvider implements ItemProvider {

    private final Menu owner;

    private ItemStack generate(ItemBuilder builder, MenuAction action) {
        ItemStack item = builder.build();
        ItemMeta meta = item.getItemMeta();

        /*
         * If the action is not null, and we haven't already defined the item action, we do not execute
         */
        if (action != null) {
            int id = owner.getActionsSize() + 1;

            /*
             * We first set the action data, so we can compare it with the already cached ones
             * The ID is set later since it is defined by our cache
             */
            ActionData data = new ActionData(meta.getDisplayName(), meta.getLore());
            action.setData(data);

            Optional<Integer> cache = owner.getActionCachedId(action);
            if (!cache.isPresent()) {
                action.setId(id);
                owner.addAction(id, action);
            } else id = cache.get();

            item = ItemUtils.addTag(item, "action", String.valueOf(id));
        }

        return item;
    }

    public ItemStack asEmpty(ItemBuilder builder) {
        return generate(builder, null);
    }

    public ItemStack asCommand(ItemBuilder builder, String command) {
        Consumer<Player> executor = (player) -> {
            TaskUtil.ensureMain(() -> {
                player.chat("/" + command);
                player.closeInventory();
            });
        };

        CraftMenuAction action = new CraftMenuAction(ClickType.LEFT, executor);
        return generate(builder, action);
    }

    public ItemStack asClickable(ItemBuilder builder, MenuAction action) {
        return generate(builder, action);
    }

    public ItemStack asNavigable(ItemBuilder builder, MenuType navigate) {
        return asClickable(builder, new CraftNavigationAction(ClickType.LEFT, navigate));
    }
}
