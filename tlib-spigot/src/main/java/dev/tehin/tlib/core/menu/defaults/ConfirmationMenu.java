package dev.tehin.tlib.core.menu.defaults;

import dev.tehin.tlib.api.menu.action.ActionExecutor;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.core.menu.MenuContentBuilder;
import dev.tehin.tlib.core.menu.MenuFilter;
import dev.tehin.tlib.core.menu.action.ExecutorAction;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class ConfirmationMenu extends Menu {

    private final String description;

    private final ActionExecutor onConfirm;
    private final ActionExecutor onCancel;

    public ConfirmationMenu(String display, String description, ActionExecutor onConfirm, ActionExecutor onCancel) {
        super(display, null);

        this.description = description;
        this.onConfirm = onConfirm;
        this.onCancel = onCancel;
    }

    @Override
    protected MenuContentBuilder create(Player player, MenuFilter filter) {
        MenuContentBuilder content = createContentBuilder();

        ItemBuilder no = new ItemBuilder(Material.WOOL)
                .data(14)
                .name("&c&lCANCELAR")
                .action(onCancel == null ? new ExecutorAction(HumanEntity::closeInventory) : new ExecutorAction(onCancel));

        ItemBuilder info = new ItemBuilder(Material.PAPER)
                .name("&7")
                .lore(description);

        ItemBuilder yes = new ItemBuilder(Material.WOOL)
                .data(5)
                .name("&a&lCONFIRMAR")
                .action(new ExecutorAction(onConfirm));

        content.add(null, no, null, null, info, null, null, yes, null);
        return content;
    }
}
