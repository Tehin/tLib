package dev.tehin.tlib.core.listener;

import dev.tehin.tlib.core.item.CraftItemManager;
import dev.tehin.tlib.core.item.InteractableItem;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.core.menu.action.ErrorAction;
import dev.tehin.tlib.core.menu.defaults.InputMenu;
import dev.tehin.tlib.core.menu.manager.CraftMenuManager;
import dev.tehin.tlib.utilities.item.ItemUtil;
import dev.tehin.tlib.utilities.task.TaskSet;
import dev.tehin.tlib.utilities.task.TaskUtil;
import net.minemora.nms.NMS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
        Optional<Menu> type = menus.getOpenMenu((Player) e.getWhoClicked());
        if (type.isEmpty()) return;

        Menu menu = type.get();

        int slot = e.getSlot();

        /*
         * Do not cancel the interaction or execute any action if
         * the menu requires an input, meaning the player can modify the slot
         * with any type of item
         */
        if (menu instanceof InputMenu inputMenu && inputMenu.isInputSlot(slot)) {
            // Update the items the tick after the transaction was completed
            TaskUtil.runSyncLater(inputMenu::updateInputItems, 1);
            return;
        }

        e.setCancelled(true);

        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == NMS.Material.AIR) return;

        Optional<String> id = ItemUtil.getTag(e.getCurrentItem(), "menu-action");

        if (id.isEmpty()) return;

        MenuAction action = menu.getActions().get(Integer.parseInt(id.get()));
        if (action.getType() != e.getClick()) return;

        Player player = (Player) e.getWhoClicked();

        if (!(action instanceof ErrorAction)) {
            player.playSound(player.getLocation(), menu.getOptions().soundOnClick(), 0.5f, 1);
        }

        action.execute(menus, (Player) e.getWhoClicked());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (item == null || item.getType() == NMS.Material.AIR) return;

        Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        Optional<InteractableItem> interactable = items.get(item);
        if (interactable.isEmpty()) return;

        interactable.get().handle(e.getPlayer());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        menus.registerClose((Player) event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        menus.registerClose(event.getPlayer());
        TaskSet.stopAll(event.getPlayer());
    }
}
