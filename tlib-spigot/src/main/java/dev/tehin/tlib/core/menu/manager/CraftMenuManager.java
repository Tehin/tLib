package dev.tehin.tlib.core.menu.manager;

import dev.tehin.tlib.api.menu.annotations.MenuMessaging;
import dev.tehin.tlib.core.LibLogger;
import dev.tehin.tlib.core.exceptions.MenuNotRegisteredException;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.api.menu.manager.MenuManager;
import dev.tehin.tlib.utilities.MessageUtil;
import dev.tehin.tlib.utilities.PermissionUtil;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CraftMenuManager implements MenuManager {

    private final HashMap<Class<? extends Menu>, Menu> menus = new HashMap<>();
    private final Map<UUID, Menu> opened = new HashMap<>();

    @Override
    public Optional<Menu> getOpenMenu(Player player) {
        if (!opened.containsKey(player.getUniqueId())) return Optional.empty();

        return Optional.of(opened.get(player.getUniqueId()));
    }

    @SneakyThrows
    @Override
    public Menu getMenu(Class<? extends Menu> type) {
        Menu menu = menus.get(type);
        if (menu == null) throw new MenuNotRegisteredException(type);

        return menu;
    }

    @SneakyThrows
    @Override
    public void register(Menu... menus) {
        for (Menu menu : menus) {
            Class<? extends Menu> clazz = menu.getClass();
            MenuMessaging messaging = clazz.getAnnotation(MenuMessaging.class);

            if (messaging != null) {
                if (messaging.noPermission().equalsIgnoreCase("hide")) {
                    menu.setNoPermissionMessage(PermissionUtil.getBukkitHelpMessage());
                } else {
                    menu.setNoPermissionMessage(messaging.noPermission());
                }
            }

            this.menus.put(clazz, menu);
        }

        LibLogger.log("Found " + menus.length + " menu instances.");
    }

    public void open(Player player, Class<? extends Menu> type) {
        Menu menu = getMenu(type);

        if (!PermissionUtil.has(player, menu.getPermission())) {
            MessageUtil.send(player, menu.getNoPermissionMessage());
            return;
        }

        menu.open(player);
    }

    public void registerOpen(Player player, Menu menu) {
        opened.put(player.getUniqueId(), menu);
    }

    public void registerClose(Player player) {
        opened.remove(player.getUniqueId());
    }

}
