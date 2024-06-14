package dev.tehin.tlib.core.material;

import dev.tehin.tlib.utilities.ErrorWrapper;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class ModernMaterialParser {

    private final static Map<String, String> newer = new HashMap<String, String>() {{
       put("STAINED_GLASS_PANE", "GRAY_STAINED_GLASS_PANE");
    }};

    public static Material parse(String name) {
        String upper = name.toUpperCase();

        return ErrorWrapper.wrap(() -> Material.valueOf(upper), Material.valueOf(newer.get(upper)));
    }
}
