package dev.tehin.tlib.core.hotbar;

import dev.tehin.tlib.api.hotbar.HotbarManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CraftHotbarManager implements HotbarManager {

    private final Map<String, Hotbar> hotbars = new HashMap<>();

    @Override
    public void register(Hotbar... hotbars) {
        for (Hotbar hotbar : hotbars) {
            this.hotbars.put(hotbar.getId(), hotbar);
        }
    }

    @Override
    public Hotbar get(String id) {
        return hotbars.get(id);
    }

    @Override
    public void give(Player player, String hotbar) {
        if (!hotbars.containsKey(hotbar)) {
            throw new IllegalArgumentException("Tried to give a hotbar that does not exist: " + hotbar);
        }

        hotbars.get(hotbar).give(player);
    }
}
