package dev.tehin.tlib.core.command;

import dev.tehin.tlib.api.command.annotation.CommandAliases;
import dev.tehin.tlib.api.command.annotation.CommandArgsStructure;
import dev.tehin.tlib.api.command.annotation.CommandDescription;
import dev.tehin.tlib.api.command.annotation.CommandProperties;
import dev.tehin.tlib.api.command.manager.CommandManager;
import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.api.command.CommandBase;
import dev.tehin.tlib.core.command.args.CommandPath;
import dev.tehin.tlib.core.command.mappings.CommandMappings;
import dev.tehin.tlib.core.command.wrapper.CommandWrapper;
import dev.tehin.tlib.core.exceptions.CommandsAlreadyRegisteredException;
import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CraftCommandManager implements CommandManager {

    private CommandMappings commands;
    private boolean registered = false;

    public CraftCommandManager() {
        Plugin plugin = tLib.get().getOwner();

        if (!(plugin.getServer().getPluginManager() instanceof SimplePluginManager)) {
            // TODO: Error due to no command support
            return;
        }

        SimplePluginManager manager = (SimplePluginManager) plugin.getServer().getPluginManager();
        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);

            this.commands = new CommandMappings((CommandMap) field.get(manager));
        } catch (IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @SneakyThrows
    @Override
    public void register(CommandBase... commands) {
        if (this.registered) throw new CommandsAlreadyRegisteredException();

        this.registered = true;

        Arrays.stream(commands).forEach(this::register);
        this.commands.load();
    }

    private void register(CommandBase command) {
        CommandProperties properties = command.getClass().getAnnotation(CommandProperties.class);
        CommandAliases aliases = command.getClass().getAnnotation(CommandAliases.class);
        CommandDescription description = command.getClass().getAnnotation(CommandDescription.class);
        CommandArgsStructure args = command.getClass().getAnnotation(CommandArgsStructure.class);

        CommandWrapper wrapper = new CommandWrapper(command, properties.executors(), new CommandPath(properties.path()));

        if (aliases != null) wrapper.setAlias(aliases.value());
        if (description != null) wrapper.setDescription(description.value());
        if (args != null) wrapper.setHardArgs(args.value());

        commands.register(wrapper);
    }

}
