package dev.tehin.tlib.core.menu.defaults;

import dev.tehin.tlib.api.menu.action.ActionExecutor;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.core.menu.MenuContentBuilder;
import dev.tehin.tlib.core.menu.MenuFilter;
import dev.tehin.tlib.core.menu.action.ConfirmAction;
import dev.tehin.tlib.core.menu.action.ExecutorAction;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minemora.nms.NMS;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class ConfirmationMenu extends Menu {

    private final String description;

    private final ItemBuilder cancelItem, confirmItem;

    public ConfirmationMenu(String display, String description, ItemBuilder confirmItem, ItemBuilder cancelItem) {
        super(display, null);

        this.description = description;

        this.confirmItem = confirmItem;
        this.cancelItem = cancelItem;
    }

    @Override
    protected MenuContentBuilder create(Player player, MenuFilter filter) {
        MenuContentBuilder content = createContentBuilder();

        ItemBuilder info = new ItemBuilder(NMS.Material.PAPER)
                .name("&7")
                .lore(description);

        content.addEmpty(9);
        content.addCentered(cancelItem, info, confirmItem);
        content.addEmpty(9);

        return content;
    }

    @Setter
    public static class Builder {

        private final String display;
        private final String description;

        private final ActionExecutor onConfirm;
        private ActionExecutor onCancel;

        private ItemBuilder confirmItem, cancelItem;

        public Builder(String display, String description, ActionExecutor onConfirm) {
            this.display = display;
            this.description = description;
            this.onConfirm = onConfirm;

            this.cancelItem = new ItemBuilder(NMS.Material.WOOL)
                    .data(14)
                    .name("&c&lCANCELAR")
                    .action(new ExecutorAction(HumanEntity::closeInventory));

            this.confirmItem = new ItemBuilder(NMS.Material.WOOL)
                    .data(5)
                    .name("&a&lCONFIRMAR")
                    .action(new ExecutorAction(onConfirm));
        }

        private void updateCancelAction() {
            this.cancelItem.action(onCancel == null ? new ExecutorAction(HumanEntity::closeInventory) : new ExecutorAction(onCancel));
        }

        public Builder cancelItem(ItemBuilder cancelItem) {
            this.cancelItem = cancelItem;
            updateCancelAction();

            return this;
        }

        public Builder confirmItem(ItemBuilder confirmItem) {
            this.confirmItem = confirmItem;
            this.confirmItem.action(new ExecutorAction(onConfirm));

            return this;
        }

        public Builder onCancel(ActionExecutor onCancel) {
            this.onCancel = onCancel;
            updateCancelAction();

            return this;
        }

        public ConfirmationMenu build() {
            return new ConfirmationMenu(display, description, confirmItem, cancelItem);
        }

    }

}
