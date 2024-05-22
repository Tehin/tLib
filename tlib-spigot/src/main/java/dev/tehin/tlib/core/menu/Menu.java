package dev.tehin.tlib.core.menu;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.craft.ItemProvider;
import dev.tehin.tlib.core.CraftLib;
import dev.tehin.tlib.core.menu.craft.CraftItemProvider;
import dev.tehin.tlib.utilities.MessageUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public abstract class Menu implements InventoryHolder {

    private @Getter @Setter CraftLib lib;

    private final HashMap<Integer, MenuAction> actions = new HashMap<>();
    protected final ItemProvider craft = new CraftItemProvider(this);

    // Display and permission is set based on the annotation when registered
    private @Setter String display;
    private @Setter @Getter String permission;

    public void open(Player player) {
        player.openInventory(get(player));
    }

    public MenuAction getAction(int id) {
        return this.actions.get(id);
    }

    public void addAction(int id, MenuAction action) {
        this.actions.put(id, action);
    }

    public Optional<Integer> getActionCachedId(MenuAction action) {
        for (MenuAction check : actions.values()) {
            if (check.equals(action)) return Optional.of(check.getId());
        }

        return Optional.empty();
    }

    protected Inventory get(Player player) {
        List<ItemStack> items = create(player);

        while (items.size() % 9 != 0) {
            items.add(null);
        }

        Inventory inventory = Bukkit.createInventory(this, items.size(), MessageUtil.color(display));
        inventory.setContents(items.toArray(new ItemStack[0]));

        return inventory;
    }

    public int getActionsSize() {
        return this.actions.size();
    }

    @Override
    public Inventory getInventory() {
        // TODO: Check to prevent unwanted inventories creation
        return Bukkit.createInventory(this, 9);
    }

    protected abstract List<ItemStack> create(Player player);

}
