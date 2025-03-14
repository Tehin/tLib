package dev.tehin.tlib.core.menu.defaults;

import dev.tehin.tlib.api.menu.features.StaticMenu;
import dev.tehin.tlib.core.menu.Menu;
import dev.tehin.tlib.core.menu.MenuContentBuilder;
import dev.tehin.tlib.core.menu.MenuFilter;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class InputMenu extends Menu implements StaticMenu {

    @Getter(AccessLevel.PROTECTED)
    private final Set<Integer> inputSlots;

    @Getter
    private final List<ItemStack> inputItems = new ArrayList<>();

    protected InputMenu(String display, String permission) {
        super(display, permission);

        this.inputSlots = createInputSlots();
        updateInputItems();
    }

    @Override
    protected MenuContentBuilder create(Player player, MenuFilter filter) {
        MenuContentBuilder wrapper = createWrapper();

        // Ensure all our input slots are empty
        for (Integer inputSlot : inputSlots) {
            wrapper.setRaw(inputSlot, null);
        }

        return wrapper;
    }

    public abstract Set<Integer> createInputSlots();

    public boolean isInputSlot(int slot) {
        return inputSlots.contains(slot);
    }

    /**
     * Creates the wrapper that will contain the inputs, which
     * are specified in {@link #createInputSlots()}
     *
     * Input spaces must be null items in the wrapper
     *
     * @return The wrapper
     */
    protected abstract MenuContentBuilder createWrapper();

    public void updateInputItems() {
        this.inputItems.clear();

        Inventory inventory = getInventory();

        int currentSize = 0;
        if (inventory != null) {
            currentSize = inventory.getSize();
        }

        for (Integer inputSlot : inputSlots) {
            if (currentSize <= inputSlot || inventory == null) {
                inputItems.add(null);
                continue;
            }

            ItemStack item = inventory.getItem(inputSlot);
            inputItems.add(item);
        }
    }

}
