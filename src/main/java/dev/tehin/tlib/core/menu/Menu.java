package dev.tehin.tlib.core.menu;

import com.avaje.ebean.validation.NotNull;
import dev.tehin.tlib.api.menu.MenuType;
import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.craft.ItemProvider;
import dev.tehin.tlib.core.menu.craft.CraftItemProvider;
import dev.tehin.tlib.utilities.MessageUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Optional;

public abstract class Menu implements InventoryHolder {

    @Getter
    private final @NotNull MenuType type;
    private final HashMap<Integer, MenuAction> actions;
    private final String display;

    protected final ItemProvider craft;

    public Menu(MenuType type, String display) {
        this.type = type;
        this.actions = new HashMap<>();
        this.craft = new CraftItemProvider(this);

        this.display = display;
    }

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
        ItemStack[] items = create(player);

        Inventory inventory = Bukkit.createInventory(this, items.length, MessageUtil.color(display));
        inventory.setContents(items);

        return inventory;
    }

    public int getActionsSize() {
        return this.actions.size();
    }

    protected abstract ItemStack[] create(Player player);

}
