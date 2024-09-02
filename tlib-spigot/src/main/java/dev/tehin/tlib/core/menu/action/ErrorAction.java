package dev.tehin.tlib.core.menu.action;

import dev.tehin.tlib.utilities.ErrorWrapper;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;

public class ErrorAction extends CraftMenuAction {

    public ErrorAction() {
        super(ClickType.LEFT, (clicker) -> {
            int volume = 1;
            float pitch = 0.7f;
            Location location = clicker.getLocation();

            /*
             * This might throw NoSuchFieldError if not found in newer versions
             * If the error is intercepted, change to fallback name (newer versions)
             */
            Sound sound = ErrorWrapper.wrapWithSupplier(() -> Sound.valueOf("NOTE_BASS"), () -> Sound.valueOf("BLOCK_NOTE_BLOCK_BASS"));

            clicker.playSound(location, sound, volume, pitch);
        });
    }

}
