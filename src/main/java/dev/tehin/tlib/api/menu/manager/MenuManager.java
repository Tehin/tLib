package dev.tehin.tlib.api.menu.manager;

import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.api.menu.MenuType;
import org.bukkit.entity.Player;

public interface MenuManager {

    void register(Menu menu);
    Menu getMenu(MenuType type);
    void open(Player player, MenuType type);
}
