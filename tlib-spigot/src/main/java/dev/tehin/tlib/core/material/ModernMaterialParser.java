package dev.tehin.tlib.core.material;

import dev.tehin.tlib.utilities.ErrorWrapper;
import org.bukkit.Material;

public class ModernMaterialParser {

    public static Material getGlassPane() {
        return ErrorWrapper.wrapWithSupplier(() -> Material.STAINED_GLASS_PANE, () -> Material.valueOf("GRAY_STAINED_GLASS_PANE"));
    }
}
