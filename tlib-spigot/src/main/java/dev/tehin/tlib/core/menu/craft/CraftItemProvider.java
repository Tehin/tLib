package dev.tehin.tlib.core.menu.craft;

import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.api.menu.action.data.ItemData;
import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.craft.ItemProvider;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.action.CraftMenuAction;
import dev.tehin.tlib.core.menu.action.CraftNavigationAction;
import dev.tehin.tlib.utilities.ErrorWrapper;
import dev.tehin.tlib.utilities.item.ItemUtil;
import dev.tehin.tlib.utilities.task.TaskUtil;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Sound;
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

        if (action == null) return item;

        /*
         * If the action is not null, and we haven't already defined the item action, we do not execute
         */
        int id = owner.getActionsSize() + 1;

        /*
         * We first set the action data, so we can compare it with the already cached ones
         * The ID is set later since it is defined by our cache
         */
        ItemData data = new ItemData(meta.getDisplayName(), meta.getLore());
        action.setData(data);

        Optional<Integer> cache = owner.getActionId(data);
        if (cache.isEmpty()) {
            action.setId(id);
            owner.addAction(id, action);
        } else {
            id = cache.get();
        }

        return ItemUtil.addTag(item, "action", String.valueOf(id));
    }

    @Override
    public ItemStack asEmpty(ItemBuilder builder) {
        return generate(builder, null);
    }

    @Override
    public ItemStack asCommand(ItemBuilder builder, String command) {
        Consumer<Player> executor = (player) -> {
            TaskUtil.runSync(() -> {
                player.chat("/" + command);
                player.closeInventory();
            });
        };

        CraftMenuAction action = new CraftMenuAction(ClickType.LEFT, executor);
        return generate(builder, action);
    }

    @Override
    public ItemStack asClickable(ItemBuilder builder, Consumer<Player> action) {
        return asClickable(builder, new CraftMenuAction(ClickType.LEFT, action));
    }

    @Override
    public ItemStack asError(ItemBuilder builder) {
        Consumer<Player> action = (clicker) -> {
            int volume = 1;
            float pitch = 0.7f;
            Location location = clicker.getLocation();

            /*
             * This might throw NoSuchFieldError if not found in newer versions
             * If the error is intercepted, change to fallback name (newer versions)
             */
            Sound sound = ErrorWrapper.wrapWithSupplier(() -> Sound.valueOf("NOTE_BASS"), () -> Sound.valueOf("BLOCK_NOTE_BLOCK_BASS"));

            clicker.playSound(location, sound, volume, pitch);
        };

        return asClickable(builder, new CraftMenuAction(ClickType.LEFT, action));
    }

    private ItemStack asClickable(ItemBuilder builder, MenuAction action) {
        return generate(builder, action);
    }

    @Override
    public ItemStack asNavigable(ItemBuilder builder, Class<? extends Menu>  navigate) {
        return asClickable(builder, new CraftNavigationAction(ClickType.LEFT, navigate));
    }
}
