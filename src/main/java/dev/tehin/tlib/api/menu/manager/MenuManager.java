package dev.tehin.tlib.api.menu.manager;

import dev.tehin.tlib.core.menu.Menu;
import org.bukkit.entity.Player;

public interface MenuManager {

    void register(Menu menu);
    Menu getMenu(Class<? extends Menu> type);
    void open(Player player, Class<? extends Menu> type);
}
