package dev.tehin.tlib.core.menu;

import dev.tehin.tlib.api.menu.action.ActionExecutor;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.action.ExecutorAction;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class ConfirmationMenu extends Menu{

    private final String display;
    private final String description;
    private final ActionExecutor action;

    public ConfirmationMenu(String display, String description, ActionExecutor action) {
        super(display, null);

        this.display = display;
        this.description = description;
        this.action = action;
    }

    @Override
    protected MenuContentBuilder create(Player player) {
        MenuContentBuilder content = createContentBuilder();

        ItemBuilder no = new ItemBuilder(Material.WOOL)
                .data(14)
                .name("&c&lCANCELAR")
                .action(new ExecutorAction(HumanEntity::closeInventory));

        ItemBuilder info = new ItemBuilder(Material.PAPER)
                .name(display)
                .lore(description);

        ItemBuilder yes = new ItemBuilder(Material.WOOL)
                .data(5)
                .name("&a&lCONFIRMAR")
                .action(new ExecutorAction(action));

        content.add(null, no, null, null, info, null, null, yes, null);
        return content;
    }
}
