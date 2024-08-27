package dev.tehin.tlib.core.menu;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.action.data.ItemData;
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

    private @Setter String display;
    private @Setter @Getter String permission;
    private @Setter @Getter String noPermissionMessage = PermissionUtil.getDefaultMessage();

    private @Getter final MenuOptions options = new MenuOptions();
    private Inventory inventory;

    public void open(Player player) {
        player.openInventory(get(player));

        player.playSound(player.getLocation(), options.soundOnOpen(), 1, 1);
    }

    public MenuAction getAction(int id) {
        return this.actions.get(id);
    }

    public void addAction(int id, MenuAction action) {
        this.actions.put(id, action);
    }

    public Optional<MenuAction> getAction(ItemData data) {
        Optional<Integer> id = getActionId(data);
        return id.map(this::getAction);
    }

    public Optional<Integer> getActionId(ItemData data) {
        for (MenuAction check : actions.values()) {
            if (check.equals(data)) return Optional.of(check.getId());
        }

        return Optional.empty();
    }

    protected Inventory get(Player player) {
        if (this instanceof StaticMenu && inventory != null) return getInventory();

        List<ItemStack> items = create(player);

        while (items.size() % 9 != 0) {
            items.add(null);
        }

        Inventory inventory = Bukkit.createInventory(this, items.size(), MessageUtil.color(display));
        inventory.setContents(items.toArray(new ItemStack[0]));

        // If the inventory has not already been created, assign it
        if (this instanceof StaticMenu) this.inventory = inventory;

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

    public void reload() {
        if (!(this instanceof StaticMenu)) {
            throw new UnsupportedOperationException("The menu is not static, please implement StaticMenu");
        }

        this.actions.clear();
        this.inventory = null;
    }

    /**
     * Updates the item in the position, changing their data and
     * lore if material is the same, if not, the ItemStack will be replaced
     * @param position Position of the item
     * @param builder Item to update
     */
    public boolean update(int position, ItemBuilder builder) {
        if (!(this instanceof StaticMenu)) {
            throw new UnsupportedOperationException("The menu is not static, please implement StaticMenu");
        }

        // Prevent updates if no one has opened the inventory yet
        if (getInventory() == null) {
            System.out.println("Item could not be updated due to inventory not being opened yet");
            return false;
        }

        ItemStack found = getInventory().getItem(position);
        if (found == null) {
            System.out.println("Item could not be updated due to position being off");
            return false;
        }

        ItemData data = new ItemData(found.getItemMeta().getDisplayName(), found.getItemMeta().getLore());

        // Get id based on our item properties
        Optional<MenuAction> action = getAction(data);
        if (action.isEmpty()) {
            System.out.println("Item could not be updated due to action not being found");
            return false;
        }

        // Set the item properties
        builder.apply(found);
        action.get().setData(new ItemData(found.getItemMeta().getDisplayName(), found.getItemMeta().getLore()));

        return true;
    }

    /**
     * Tries to find the position based on the given item, then executes
     * {@link Menu#update(int, ItemBuilder)} with the {@link ItemBuilder} and its position
     * @param builder ItemStack that will replace or update the existent one
     * @return If the {@link ItemStack} was found and replaced
     */
    public boolean update(ItemBuilder builder) {
        throw new UnsupportedOperationException("Not implemented yet, please use Menu#update(int, ItemBuilder)");
    }

}
