package dev.tehin.tlib.core.command;

import dev.tehin.tlib.api.command.CommandBase;
import dev.tehin.tlib.api.command.annotation.CommandAliases;
import dev.tehin.tlib.api.command.annotation.CommandArgsStructure;
import dev.tehin.tlib.api.command.annotation.CommandDescription;
import dev.tehin.tlib.api.command.annotation.CommandProperties;
import dev.tehin.tlib.api.command.manager.CommandManager;
import dev.tehin.tlib.core.command.args.CommandPath;
import dev.tehin.tlib.core.command.mappings.CommandMappings;
import dev.tehin.tlib.core.command.wrapper.CommandWrapper;
import dev.tehin.tlib.core.exceptions.CommandsAlreadyRegisteredException;
import dev.tehin.tlib.core.exceptions.NoPropertiesFoundException;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;

import java.util.Arrays;

public class CraftCommandManager implements CommandManager {

    private CommandMappings commands = new CommandMappings();
    private boolean registered = false;

    @SneakyThrows
    @Override
    public void register(CommandBase... commands) {
        if (this.registered) throw new CommandsAlreadyRegisteredException();

        this.registered = true;

        Arrays.stream(commands).forEach(this::register);
        this.commands.load();
    }

    @SneakyThrows
    private void register(CommandBase command) {
        CommandProperties properties = command.getClass().getAnnotation(CommandProperties.class);
        if (properties == null) throw new NoPropertiesFoundException(CommandBase.class);

        // These are not required
        CommandAliases aliases = command.getClass().getAnnotation(CommandAliases.class);
        CommandDescription description = command.getClass().getAnnotation(CommandDescription.class);
        CommandArgsStructure args = command.getClass().getAnnotation(CommandArgsStructure.class);

        CommandWrapper wrapper = new CommandWrapper(command, properties.executors(), properties.permission(), new CommandPath(properties.path()));

        if (aliases != null) wrapper.setAlias(aliases.value());
        if (description != null) wrapper.setDescription(description.value());
        if (args != null) {
            wrapper.setHardArgs(args.structure());
            wrapper.setFixedLength(args.fixedLength());
            wrapper.setUsage(args.usage());
        }

        commands.register(wrapper);
    }

}
