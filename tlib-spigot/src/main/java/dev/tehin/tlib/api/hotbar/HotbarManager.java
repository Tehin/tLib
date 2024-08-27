package dev.tehin.tlib.api.hotbar;

import dev.tehin.tlib.core.hotbar.Hotbar;
import org.bukkit.entity.Player;

public interface HotbarManager {

    void register(Hotbar... hotbars);
    void give(Player player, String hotbar);

}
