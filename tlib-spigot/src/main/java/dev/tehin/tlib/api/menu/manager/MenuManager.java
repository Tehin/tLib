package dev.tehin.tlib.api.menu.manager;

import dev.tehin.tlib.core.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public interface MenuManager {

    /**
     * Registers the {@link Menu} for later use
     * @param menus Array of {@link Menu} to be registered
     */
    void register(Menu... menus);

    HashMap<UUID, Menu> getAllOpen();

    /**
     * Gets the {@link Menu} based on its class
     * @param type Class representing the {@link Menu}
     * @return The found {@link Menu}, or null if not registered
     */
    Menu getMenu(Class<? extends Menu> type);

    /**
     * Gets the open {@link Menu} of the specified player
     * @param player The player
     * @return The found {@link Menu}, or empty if not found
     */
    Optional<Menu> getOpenMenu(Player player);

    /**
     * Opens the specified {@link Menu} to the player, with
     * the registered actions listening
     * @param player Player who will be targeted
     * @param type Class representing the {@link Menu}
     */
    void open(Player player, Class<? extends Menu> type);
}
