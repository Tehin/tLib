package dev.tehin.tlib.api.command.annotation;

import org.bukkit.command.TabCompleter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandCompletion {

    Class<? extends TabCompleter> value();

}
