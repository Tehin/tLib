package dev.tehin.tlib.core.listener;

import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.item.CraftItemManager;
import dev.tehin.tlib.core.item.InteractableItem;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.core.menu.manager.CraftMenuManager;
import dev.tehin.tlib.utilities.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class CoreListener implements Listener {

    private final CraftMenuManager menus;
    private final CraftItemManager items;

    public CoreListener(CraftMenuManager menus, CraftItemManager items) {
        this.menus = menus;
        this.items = items;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Optional<Menu> type = menus.getMenu(e.getInventory());
        if (type.isEmpty() || e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

        e.setCancelled(true);

        Menu menu = type.get();
        Optional<String> id = ItemUtil.getTag(e.getCurrentItem(), "action");

        if (id.isEmpty()) return;

        MenuAction action = menu.getAction(Integer.parseInt(id.get()));
        if (action.getType() != e.getClick()) return;

        action.execute(menus, (Player) e.getWhoClicked());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (item == null || item.getType() == Material.AIR) return;

        Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        Optional<InteractableItem> interactable = items.get(item);
        if (interactable.isEmpty()) return;

        interactable.get().handle(e.getPlayer());
    }
}