package dev.tehin.tlib.utilities;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class PermissionUtil {

    public static boolean has(CommandSender sender, String permission) {
        if (permission == null || permission.isEmpty()) return true;
        if (sender instanceof ConsoleCommandSender) return true;

        return sender.hasPermission(permission);
    }

    public static String getDefaultMessage() {
        return "&cYou don't have enough permissions to do this.";
    }

    public static String getBukkitHelpMessage() {
        return Bukkit.spigot().getConfig().getString("messages.unknown-command");
    }
}
