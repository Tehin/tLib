package dev.tehin.tlib.core.lang;

import dev.tehin.tlib.api.lang.LangProvider;
import dev.tehin.tlib.api.tLib;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LangParser {

    public static String parse(Player player, String string) {
        LangProvider provider = tLib.get().getConfig().langProvider();

        if (provider == null) return string;

        Pattern pattern = Pattern.compile("<lang=([A-Za-zÀ-ȕ0-9-_.]+)>");
        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            String key = matcher.group(1);
            String lang = provider.getLang(player, key);

            if (lang == null) {
                throw new IllegalStateException("Could not find translation for " + key + " for player " + player.getName());
            }

            string = string.replace("<lang=" + key + ">", lang);
        }

        return string;
    }

}
