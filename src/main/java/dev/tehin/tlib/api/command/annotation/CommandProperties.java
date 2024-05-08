package dev.tehin.tlib.api.command.annotation;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public @interface CommandProperties {

    String path();
    String permission() default "";
    Class<? extends CommandSender>[] executors() default { Player.class, ConsoleCommandSender.class };

}
