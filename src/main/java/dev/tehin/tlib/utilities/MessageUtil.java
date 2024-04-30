package dev.tehin.tlib.utilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static void send(Player player, String message) {
        player.sendMessage(color(message));
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
