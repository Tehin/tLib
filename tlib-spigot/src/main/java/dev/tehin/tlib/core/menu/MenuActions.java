package dev.tehin.tlib.core.menu;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.action.data.ItemData;

import java.util.HashMap;
import java.util.Optional;

public class MenuActions {

    private final HashMap<Integer, MenuAction> actions = new HashMap<>();

    public MenuAction get(int id) {
        return this.actions.get(id);
    }

    public void set(int id, MenuAction action) {
        this.actions.put(id, action);
    }

    public Optional<MenuAction> get(ItemData data) {
        for (MenuAction check : actions.values()) {
            if (check.equals(data)) return Optional.of(check);
        }

        return Optional.empty();
    }

    public int getNextId() {
        return this.actions.size() + 1;
    }

    public void clear() {
        this.actions.clear();
    }
}
