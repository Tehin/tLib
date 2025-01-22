package dev.tehin.tlib.core.lang;

import dev.tehin.tlib.api.lang.LangProvider;
import dev.tehin.tlib.api.tLib;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LangParser {

    public static String parse(Player player, String string) {
        LangProvider provider = tLib.get().getConfig().langProvider();

        if (provider == null || !string.contains("<lang=")) return string;

        StringBuilder builder = new StringBuilder();

        Pattern pattern = Pattern.compile("<lang=([A-Za-zÀ-ȕ0-9-_]+)>");
        Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            String key = matcher.group(1);
            String lang = provider.getLang(player, key);

            if (lang == null) continue;

            matcher.appendReplacement(builder, lang);
        }

        return builder.toString();
    }

}
