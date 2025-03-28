package dev.tehin.tlib.utilities;

import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.lang.LangParser;
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
        if (sender instanceof Player) {
            message = LangParser.parse((Player) sender, message);
        }

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
        String text = component.getText();

        String parsed = LangParser.parse(player, text);
        component.setText(parsed);

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

    public static void broadcast(String message) {
        Bukkit.broadcastMessage(color(message));
    }

    public static void broadcastFormal(String message) {
        String prefix = tLib.get().getConfig().messaging().getPrefix();
        broadcast(prefix + " " + message);
    }

    public static String convertToOwnColorCode(String message) {
        return message.replace("§", "&");
    }

}
