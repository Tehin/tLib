package dev.tehin.tlib.utilities;

import net.minemora.nms.NMS;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.material.Dye;

import java.util.HashMap;
import java.util.Map;

public class DyeUtil {

    private static final Map<ChatColor, DyeColor> chatToDye = new HashMap<ChatColor, DyeColor>() {{
        put(ChatColor.BLACK, NMS.DyeColor.BLACK);
        put(ChatColor.WHITE, NMS.DyeColor.WHITE);
        put(ChatColor.DARK_RED, NMS.DyeColor.RED);
        put(ChatColor.RED, NMS.DyeColor.MAGENTA);
        put(ChatColor.DARK_BLUE, NMS.DyeColor.BLUE);
        put(ChatColor.BLUE, NMS.DyeColor.LIGHT_BLUE);
        put(ChatColor.AQUA, NMS.DyeColor.CYAN);
        put(ChatColor.DARK_AQUA, NMS.DyeColor.LIGHT_BLUE);
        put(ChatColor.DARK_GREEN, NMS.DyeColor.GREEN);
        put(ChatColor.GREEN, NMS.DyeColor.LIME);
        put(ChatColor.DARK_PURPLE, NMS.DyeColor.PURPLE);
        put(ChatColor.LIGHT_PURPLE, NMS.DyeColor.PINK);
        put(ChatColor.GRAY, NMS.DyeColor.LIGHT_BLUE);
        put(ChatColor.DARK_GRAY, NMS.DyeColor.GRAY);
        put(ChatColor.GOLD, NMS.DyeColor.ORANGE);
        put(ChatColor.YELLOW, NMS.DyeColor.YELLOW);
    }};

    public static DyeColor fromChat(ChatColor color) {
        return chatToDye.get(color);
    }

}
