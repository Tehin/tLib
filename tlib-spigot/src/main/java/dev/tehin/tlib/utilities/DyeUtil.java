package dev.tehin.tlib.utilities;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.material.Dye;

import java.util.HashMap;
import java.util.Map;

public class DyeUtil {

    private static final Map<ChatColor, DyeColor> chatToDye = new HashMap<ChatColor, DyeColor>() {{
        put(ChatColor.BLACK, DyeColor.BLACK);
        put(ChatColor.WHITE, DyeColor.WHITE);
        put(ChatColor.DARK_RED, DyeColor.RED);
        put(ChatColor.RED, DyeColor.MAGENTA);
        put(ChatColor.DARK_BLUE, DyeColor.BLUE);
        put(ChatColor.BLUE, DyeColor.LIGHT_BLUE);
        put(ChatColor.AQUA, DyeColor.CYAN);
        put(ChatColor.DARK_AQUA, DyeColor.LIGHT_BLUE);
        put(ChatColor.DARK_GREEN, DyeColor.GREEN);
        put(ChatColor.GREEN, DyeColor.LIME);
        put(ChatColor.DARK_PURPLE, DyeColor.PURPLE);
        put(ChatColor.LIGHT_PURPLE, DyeColor.PINK);
        put(ChatColor.GRAY, DyeColor.SILVER);
        put(ChatColor.DARK_GRAY, DyeColor.GRAY);
        put(ChatColor.GOLD, DyeColor.ORANGE);
        put(ChatColor.YELLOW, DyeColor.YELLOW);
    }};

    public static DyeColor fromChat(ChatColor color) {
        return chatToDye.get(color);
    }

}
