package dev.tehin.tlib.api.menu.action;

import dev.tehin.tlib.api.menu.action.data.ItemData;
import dev.tehin.tlib.api.menu.manager.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.function.Consumer;

public interface MenuAction {

    /**
     * @return The Bukkit click type from the player interaction
     */
    ClickType getType();

    /**
     * @return The action to be executed to the player
     */
    ActionExecutor getAction();

    /**
     * @return The interaction unique id
     */
    int getId();

    /**
     * @param id The interaction id to be set
     */
    void setId(int id);

    /**
     * @return Metadata of the action (item name, lore and more)
     */
    ItemData getData();

    /**
     * @param data The data to be set
     */
    void setData(ItemData data);

    /**
     * Executes the action for the specified player
     * @param player The player who clicked
     */
    void execute(MenuManager manager, Player player);

    /**
     * Compares if two actions are the same, based on the item
     * @param comparator Action to be compared to
     * @return If both actions are equal
     */
    boolean equals(MenuAction comparator);

    /**
     * Compares if two actions are the same, based on the item
     * @param comparator Action to be compared to
     * @return If both actions are equal
     */
    boolean equals(ItemData comparator);
}
