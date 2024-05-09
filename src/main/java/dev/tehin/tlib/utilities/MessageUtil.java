package dev.tehin.tlib.utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static void send(CommandSender player, String message) {
        player.sendMessage(color(message));
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void sendNoPermission(CommandSender player) {
        send(player, "&cYou don't have enough permissions to do this.");
    }
}
