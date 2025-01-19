package dev.tehin.tlib.api.hotbar;

import dev.tehin.tlib.core.hotbar.HotbarItem;
import org.bukkit.entity.Player;

public interface HotbarItemCondition {

    boolean canGive(Player player);

}
