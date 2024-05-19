package dev.tehin.tlib.utilities;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PermissionUtil {

    public static boolean has(CommandSender sender, String permission) {
        if (permission == null || permission.isEmpty()) return true;
        if (!(sender instanceof ProxiedPlayer)) return true;

        return sender.hasPermission(permission);
    }

    public static void sendMessage(CommandSender sender) {
        MessageUtil.send(sender, "&cYou don't have enough permissions to do this.");
    }
}
