package dev.tehin.tlib.core.hotbar;

import dev.tehin.tlib.api.tLib;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class Hotbar {

    private final String id;
    private final Map<Integer, String> items = new HashMap<>();

    public void add(int slot, String item) {
        if (tLib.get().getItem().get(item).isEmpty()) {
            throw new IllegalArgumentException("Tried to add an item that does not exist: " + item);
        }

        this.items.put(slot, item);
    }

    public void give(Player player) {
        items.forEach((slot, item) -> tLib.get().getItem().give(player, item, slot));
    }
}
