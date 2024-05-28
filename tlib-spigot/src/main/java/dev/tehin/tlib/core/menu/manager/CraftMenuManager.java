package dev.tehin.tlib.core.menu.manager;

import dev.tehin.tlib.api.menu.annotations.MenuMessaging;
import dev.tehin.tlib.api.menu.annotations.MenuProperties;
import dev.tehin.tlib.core.CraftLib;
import dev.tehin.tlib.core.exceptions.MenuNotRegisteredException;
import dev.tehin.tlib.core.exceptions.NoPropertiesFoundException;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.api.menu.manager.MenuManager;
import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.menu.listener.MenuListener;
import dev.tehin.tlib.utilities.MessageUtil;
import dev.tehin.tlib.utilities.PermissionUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Optional;

public class CraftMenuManager implements MenuManager {

    private final HashMap<Class<? extends Menu>, Menu> menus = new HashMap<>();
    private @Getter final CraftLib lib;

    public CraftMenuManager(CraftLib lib) {
        this.lib = lib;

        Bukkit.getPluginManager().registerEvents(new MenuListener(this), lib.getOwner());
    }

    /**
     * Safely get the Menu owner of specified inventory
     * @param inventory The inventory we are getting the Menu from
     * @return Empty {@link Optional} if not found, or wrapping {@link Menu} if found
     */
    public Optional<Menu> getMenu(Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof Menu)) return Optional.empty();

        return Optional.of((Menu) holder);
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
            MenuProperties properties = clazz.getAnnotation(MenuProperties.class);
            MenuMessaging messaging = clazz.getAnnotation(MenuMessaging.class);

            if (properties == null) throw new NoPropertiesFoundException(menu.getClass());

            menu.setDisplay(properties.display());
            menu.setPermission(properties.permission());
            menu.setLib(lib);

            if (messaging != null) {
                menu.setNoPermissionMessage(messaging.noPermission());
            }

            this.menus.put(clazz, menu);
        }
    }

    public void open(Player player, Class<? extends Menu> type) {
        Menu menu = getMenu(type);

        if (!PermissionUtil.has(player, menu.getPermission())) {
            MessageUtil.send(player, menu.getNoPermissionMessage());
            return;
        }

        menu.open(player);
    }


}
