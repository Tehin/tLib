package dev.tehin.tlib.core.menu.listener;

import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.core.menu.manager.CraftMenuManager;
import dev.tehin.tlib.utilities.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;

public class MenuListener implements Listener {

    private final CraftMenuManager manager;

    public MenuListener(CraftMenuManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Optional<Menu> type = manager.getMenu(e.getInventory());
        if (!type.isPresent() || e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

        e.setCancelled(true);

        Menu menu = type.get();
        Optional<String> id = ItemUtil.getTag(e.getCurrentItem(), "action");

        if (!id.isPresent()) return;

        MenuAction action = menu.getAction(Integer.parseInt(id.get()));
        if (action.getType() != e.getClick()) return;

        Player player = (Player) e.getWhoClicked();
        action.execute(manager, player);

        player.playSound(player.getLocation(), menu.getOptions().soundOnClick(), 1, 1);
    }
}
