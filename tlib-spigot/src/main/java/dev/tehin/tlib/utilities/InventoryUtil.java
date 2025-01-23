package dev.tehin.tlib.utilities;

import net.minemora.nms.NMS;
import org.bukkit.entity.Player;

public class InventoryUtil {

    public static void update(Player player, String title, int newSize) {
        NMS.get().getUtil().updateInventoryTitle(player, MessageUtil.color(title), newSize);
    }

}
