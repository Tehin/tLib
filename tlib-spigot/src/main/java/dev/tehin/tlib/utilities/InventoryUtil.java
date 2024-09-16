package dev.tehin.tlib.utilities;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryView;
import org.bukkit.entity.Player;

public class InventoryUtil {

    public static void update(Player player, String title, int newSize) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();

        CraftInventoryView inv = (CraftInventoryView) player.getOpenInventory();
        if (newSize == -1) inv.getTopInventory().getSize();

        int windowId = inv.getHandle().windowId;

        handle.playerConnection.sendPacket(new PacketPlayOutOpenWindow(windowId, "minecraft:container", new ChatComponentText(MessageUtil.color(title)), newSize));
    }

}
