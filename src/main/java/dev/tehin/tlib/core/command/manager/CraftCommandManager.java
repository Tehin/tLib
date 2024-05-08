package dev.tehin.tlib.core.command.manager;

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
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class CraftCommandManager implements CommandManager {

    private final CommandMappings commands = new CommandMappings();
    private CommandMap bukkitMap;

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
            this.bukkitMap = (CommandMap) field.get(manager);
        } catch (IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(CommandBase command) {
        CommandProperties properties = command.getClass().getAnnotation(CommandProperties.class);
        CommandAliases aliases = command.getClass().getAnnotation(CommandAliases.class);
        CommandDescription description = command.getClass().getAnnotation(CommandDescription.class);
        CommandArgsStructure args = command.getClass().getAnnotation(CommandArgsStructure.class);

        CommandWrapper wrapper = new CommandWrapper(command, properties.executors(), new CommandPath(properties.path()));

        if (aliases != null) wrapper.setAlias(aliases.value());
        if (description != null) wrapper.setDescription(description.value());
        if (args != null) wrapper.setHardArgs(args.value());

        create(wrapper);
    }

    // TODO: Move to a provider
    private void create(CommandWrapper wrapper) {
        commands.register(wrapper);

        String main = wrapper.getPath().getParentCommand();

        // If the command has a main that is already registered, add the sub-command to it
        if (commands.get(main).isPresent()) {
            // TODO: Register sub-command to the wrapper
            return;
        }

        // TODO: This should create only the main command
        //      if a command has no children, register that instead
        Command bukkit = new Command(main) {
            @Override
            public boolean execute(CommandSender sender, String label, String[] args) {
                // TODO: Logic for executing subcommands:
                //      create a map somewhere that maps the subcommands
                //      so they can be get and executed from here?
                return wrapper.execute(sender, label, args);
            }
        };

        // Register command to the server map
        bukkitMap.register(main, bukkit);
    }
}
