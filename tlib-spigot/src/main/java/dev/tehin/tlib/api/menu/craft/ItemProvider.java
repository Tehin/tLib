package dev.tehin.tlib.api.menu.craft;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface ItemProvider {

    /**
     * Creates an item that will have no action, this is
     * not necessary since not registered items will have
     * the same result
     * @param builder Builder that will create the {@link ItemStack} displayed
     * @return {@link ItemStack} that has been created
     */
    ItemStack asEmpty(ItemBuilder builder);

    /**
     * Creates an item that will make the player
     * execute a command when clicked
     * @param builder Builder that will create the {@link ItemStack} displayed
     * @param command The command to be executed by the player
     * @return {@link ItemStack} that has been created and registered
     */
    ItemStack asCommand(ItemBuilder builder, String command);

    /**
     * Creates an item that will execute any action when a
     * player interacts with the specified click
     * @param builder Builder that will create the {@link ItemStack} displayed
     * @param action The action instance to be executed
     * @return {@link ItemStack} that has been created and registered
     */
    ItemStack asClickable(ItemBuilder builder, Consumer<Player> action);

    /**
     * Creates an item that will navigate through menus when
     * clicked, the specified inventory will be opened
     * @param builder Builder that will create the {@link ItemStack} displayed
     * @param navigate The inventory to open
     * @return {@link ItemStack} that has been created and registered
     */
    ItemStack asNavigable(ItemBuilder builder, Class<? extends Menu> navigate);
}
