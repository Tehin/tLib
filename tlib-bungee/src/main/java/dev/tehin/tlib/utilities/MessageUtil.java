package dev.tehin.tlib.utilities;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Arrays;
import java.util.Collection;

public class MessageUtil {

    public static BaseComponent format(String msg) {
        return TextComponent.fromLegacy(color(msg));
    }

    public static void send(CommandSender sender, String msg) {
        sender.sendMessage(format(msg));
    }

    public static void send(CommandSender sender, String[] messages) {
        Arrays.stream(messages).forEach((msg) -> send(sender, msg));
    }

    public static void send(CommandSender sender, Collection<String> messages) {
        messages.forEach((msg) -> send(sender, msg));
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
