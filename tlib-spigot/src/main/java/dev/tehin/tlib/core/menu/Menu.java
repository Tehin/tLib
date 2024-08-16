package dev.tehin.tlib.core.menu;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.craft.ItemProvider;
import dev.tehin.tlib.api.menu.features.StaticMenu;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.craft.CraftItemProvider;
import dev.tehin.tlib.utilities.MessageUtil;
import dev.tehin.tlib.utilities.PermissionUtil;
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

    private final HashMap<Integer, MenuAction> actions = new HashMap<>();
    protected final ItemProvider craft = new CraftItemProvider(this);

    // Display and permission is set based on the annotation when registered
    private @Setter String display;
    private @Setter @Getter String permission;
    private @Setter @Getter String noPermissionMessage = PermissionUtil.getDefaultMessage();

    private Inventory inventory = Bukkit.createInventory(this, 9);

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
        if (this instanceof StaticMenu) return getInventory();

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
        return inventory;
    }

    protected abstract List<ItemStack> create(Player player);

    /**
     * Updates the item in the position, changing their data and
     * lore if material is the same, if not, the ItemStack will be replaced
     * @param position Position of the item
     * @param builder Item to update
     */
    public void update(int position, ItemBuilder builder) {
    }

    /**
     * Tries to find the position based on the given item, then executes
     * {@link Menu#update(int, ItemBuilder)} with the {@link ItemBuilder} and its position
     * @param builder ItemStack that will replace or update the existent one
     * @return If the {@link ItemStack} was found and replaced
     */
    public boolean update(ItemBuilder builder) {
        return false;
    }

}
