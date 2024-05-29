package dev.tehin.tlib.api.command.annotation;

import org.bukkit.command.TabCompleter;

public @interface CommandCompletion {

    Class<? extends TabCompleter> value();

}
