package dev.tehin.tlib.api.command.annotation;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandProperties {

    String path();
    String permission() default "";
    Class<? extends CommandSender>[] executors() default { ProxiedPlayer.class };

}
