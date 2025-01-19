package dev.tehin.tlib.api.hotbar;

import dev.tehin.tlib.core.hotbar.Hotbar;
import org.bukkit.entity.Player;

public interface HotbarManager {

    void register(Hotbar... hotbars);
    Hotbar get(String id);
    void give(Player player, String hotbar);

}
