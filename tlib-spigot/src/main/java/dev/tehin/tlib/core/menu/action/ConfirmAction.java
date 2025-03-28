package dev.tehin.tlib.core.menu.action;

import dev.tehin.tlib.api.menu.action.ActionExecutor;
import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.core.item.ItemBuilder;
import dev.tehin.tlib.core.menu.defaults.ConfirmationMenu;
import lombok.RequiredArgsConstructor;
import net.minemora.nms.NMS;
import org.bukkit.entity.HumanEntity;

public class ConfirmAction extends ExecutorAction {

    private ConfirmAction(String display, String description, ItemBuilder confirmItem, ItemBuilder cancelItem) {
        super((clicker) -> {
            new ConfirmationMenu(display, description, confirmItem, cancelItem).open(clicker);
        });
    }

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

        public ConfirmAction build() {
            return new ConfirmAction(display, description, confirmItem, cancelItem);
        }

    }

}
