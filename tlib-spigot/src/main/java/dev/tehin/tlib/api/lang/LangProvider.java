package dev.tehin.tlib.api.lang;

import org.bukkit.entity.Player;

public interface LangProvider {

    String getLang(Player player, String key);

}
