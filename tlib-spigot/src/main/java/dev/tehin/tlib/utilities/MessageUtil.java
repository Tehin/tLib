package dev.tehin.tlib.utilities;

import dev.tehin.tlib.api.tLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageUtil {

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    /**
     * Sends the message with the defined prefix if configured
     * @param lib Owner library
     * @param sender Target of the message
     * @param message Message to be displayed
     */
    public static void sendFormal(tLib lib, CommandSender sender, String message) {
        String prefix = lib.getConfig().messaging().getPrefix();

        send(sender, prefix + " " + message);
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void sendConsole(String message) {
        send(Bukkit.getConsoleSender(), message);
    }

}
