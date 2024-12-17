package dev.tehin.tlib.core.menu.action;

import dev.tehin.tlib.utilities.ErrorWrapper;
import net.minemora.nms.NMS;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;

public class ErrorAction extends CraftMenuAction {

    public ErrorAction() {
        super(ClickType.LEFT, (clicker) -> {
            int volume = 1;
            float pitch = 0.7f;
            Location location = clicker.getLocation();

            Sound sound = NMS.Sound.NOTE_BASS;

            clicker.playSound(location, sound, volume, pitch);
        });
    }

}
