package dev.tehin.tlib.utilities;

import dev.tehin.tlib.api.tLib;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MessageUtil {

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    public static void send(CommandSender sender, String[] messages) {
        Arrays.stream(messages).forEach(msg -> send(sender, msg));
    }

    public static void send(CommandSender sender, Collection<String> messages) {
        for (String message : messages) {
            send(sender, message);
        }
    }


    public static void send(Player player, TextComponent component) {
        player.spigot().sendMessage(component);
    }

    /**C
     * Sends the message with the defined prefix if configured
     * @param sender Target of the message
     * @param message Message to be displayed
     */
    public static void sendFormal(CommandSender sender, String message) {
        String prefix = tLib.get().getConfig().messaging().getPrefix();

        send(sender, prefix + " " + message);
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String strip(String msg) {
        return ChatColor.stripColor(color(msg));
    }

    public static void sendConsole(String message) {
        send(Bukkit.getConsoleSender(), message);
    }

}
