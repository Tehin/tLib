package dev.tehin.tlib.api.menu.manager;

import dev.tehin.tlib.core.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface MenuManager {

    /**
     * Registers the {@link Menu} for later use
     * @param menu Menu to be registered
     */
    void register(Menu menu);

    /**
     * Gets the {@link Menu} based on its class
     * @param type Class representing the {@link Menu}
     * @return The found {@link Menu}, or null if not registered
     */
    Menu getMenu(Class<? extends Menu> type);

    /**
     * Opens the specified {@link Menu} to the player, with
     * the registered actions listening
     * @param player Player who will be targeted
     * @param type Class representing the {@link Menu}
     */
    void open(Player player, Class<? extends Menu> type);
}
