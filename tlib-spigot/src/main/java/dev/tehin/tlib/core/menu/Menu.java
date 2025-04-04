package dev.tehin.tlib.core.menu;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.action.data.ItemData;
import dev.tehin.tlib.api.menu.features.PageableMenu;
import dev.tehin.tlib.api.menu.features.StaticMenu;
import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.lang.LangParser;
import dev.tehin.tlib.core.menu.manager.CraftMenuManager;
import dev.tehin.tlib.core.menu.options.MenuOptions;
import dev.tehin.tlib.core.menu.templates.EmptyMenuTemplate;
import dev.tehin.tlib.core.menu.templates.PageableMenuTemplate;
import dev.tehin.tlib.utilities.MessageUtil;
import dev.tehin.tlib.utilities.PermissionUtil;
import lombok.Getter;
import lombok.Setter;
import net.minemora.nms.NMS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public abstract class Menu implements InventoryHolder {

    @Getter
    private final MenuActions actions = new MenuActions();

    private final String display;
    private final @Getter String permission;

    private @Setter @Getter String noPermissionMessage = PermissionUtil.getDefaultMessage();

    private @Getter final MenuOptions options = new MenuOptions();

    @Getter
    private Inventory inventory;

    protected Menu(String display, String permission) {
        this.display = display;
        this.permission = permission;
    }

    protected abstract MenuContentBuilder create(Player player, MenuFilter filter);

    protected MenuContentBuilder createContentBuilder() {
        return createContentBuilder(null);
    }

    protected MenuContentBuilder createContentBuilder(Player player) {
        if (player != null && this instanceof StaticMenu) {
            throw new IllegalStateException("Cannot set the player for a static menu");
        }

        return new MenuContentBuilder(this, player);
    }

    public void open(Player player) {
        open(player, 0, MenuFilter.ALL);
    }

    public void open(Player player, int page, MenuFilter filter) {
        if (!(this instanceof PageableMenu) && page > 0) {
            throw new IllegalStateException("Not pageable menus cannot be opened with a page greater than 1, please implement PageableMenu");
        }

        player.playSound(player.getLocation(), getOptions().soundOnOpen(), 0.5f, 1f);

        boolean isPageable = this instanceof PageableMenu;
        boolean isStatic = this instanceof StaticMenu;

        if (isStatic && isPageable) {
            throw new UnsupportedOperationException("Pageable statics menus are not supported yet");
        }

        if (isStatic && inventory != null) {
            player.openInventory(getInventory());
            registerOpen(player);
            return;
        }

        List<ItemStack> items = get(player, page, filter);

        // If the inventory has not already been created, assign it
        // We avoid checking this in the first if statement to improve performance since
        // we don't want to create items that will not be used on a static inventory
        if (isStatic) {
            this.inventory = createInventory(player, items);

            // Open using the cached inventory
            player.openInventory(getInventory());
            registerOpen(player);
            return;
        }

        // Open without closing the last inventory
        player.openInventory(createInventory(player, items));
        registerOpen(player);
    }

    // Register that the player opened the inventory, override if one is already opened
    // Use this function separately on each case to avoid unintentional unregister
    private void registerOpen(Player player) {
        CraftMenuManager manager = (CraftMenuManager) tLib.get().getMenu();
        manager.registerOpen(player, this);
    }

    protected List<ItemStack> get(Player player, int page, MenuFilter filter) {
        List<ItemStack> items = create(player, filter).build(getTemplate(filter, page));
        if (items.size() % 9 != 0) {
            throw new IllegalStateException("Menu size '" + items.size() + "' is not a multiple of 9");
        }

        return items;
    }

    private Inventory createInventory(Player player, List<ItemStack> items) {
        String title = LangParser.parse(player, MessageUtil.color(display));

        Inventory inventory = Bukkit.createInventory(this, items.size(), title);
        inventory.setContents(items.toArray(new ItemStack[0]));

        return inventory;
    }

    public void reload() {
        if (!(this instanceof StaticMenu)) {
            throw new UnsupportedOperationException("The menu is not static, please implement StaticMenu");
        }

        this.actions.clear();
        this.inventory = null;

        // Clear the inventory for all the players
        closeAll();
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
            return false;
        }

        ItemStack foundItem = getInventory().getItem(position);
        if (foundItem == null) {
            System.out.println("Item could not be updated due to position being off");
            return false;
        }

        Optional<String> actionId = NMS.get().getUtil().getNBT(foundItem, "menu-action");
        if (actionId.isEmpty()) {
            System.out.println("Item could not be updated due to action NBT (Current: null)");
            return false;
        }

        MenuAction action = getActions().get(Integer.parseInt(actionId.get()));
        if (action == null) {
            System.out.println("Item could not be updated due to action not being found (Current: " + actionId + ")");
            return false;
        }

        // Set the item properties
        builder.apply(foundItem);

        // Apply the data of new item to the action so we can compare it later
        action.setData(ItemData.of(foundItem));

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

    public MenuTemplate getTemplate(MenuFilter filter, int page) {
        return (this instanceof PageableMenu) ? new PageableMenuTemplate(this, filter, page) : new EmptyMenuTemplate();
    }

    public void closeAll() {
        tLib.get().getMenu().getAllOpen().forEach((uuid, menu) -> {
            if (!menu.equals(this)) return;

            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                player.closeInventory();
            }
        });
    }

    public void onOpen(Player player) {
    }

    public void onClose(Player player) {
    }

    public void onClick(InventoryClickEvent event) {
    }

}
