package dev.tehin.tlib.core.command.manager;

import dev.tehin.tlib.api.command.annotation.CommandAliases;
import dev.tehin.tlib.api.command.annotation.CommandDescription;
import dev.tehin.tlib.api.command.annotation.CommandProperties;
import dev.tehin.tlib.api.command.manager.CommandManager;
import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.command.CommandBase;
import dev.tehin.tlib.core.command.wrapper.CommandWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class CraftCommandManager implements CommandManager {

    private final HashMap<String, CommandWrapper> commands = new HashMap<>();
    private final HashMap<String, String> aliasesResolver = new HashMap<>();
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

    public Optional<CommandWrapper> getCommand(String main) {
        String alias = aliasesResolver.get(main);

        CommandWrapper wrapper;

        if (alias != null) {
            wrapper = commands.get(alias);
        } else {
            wrapper = commands.get(main);
        }

        return Optional.ofNullable(wrapper);
    }

    public void register(CommandBase command) {
        CommandProperties properties = command.getClass().getAnnotation(CommandProperties.class);
        CommandAliases aliases = command.getClass().getAnnotation(CommandAliases.class);
        CommandDescription description = command.getClass().getAnnotation(CommandDescription.class);

        CommandWrapper wrapper = new CommandWrapper(command, properties.executors(), properties.path());

        if (aliases != null) {
            wrapper.setAlias(aliases.value());

            Arrays.stream(aliases.value()).forEach(alias -> aliasesResolver.put(alias, wrapper.getPath()));
        }

        if (description != null) wrapper.setDescription(description.value());

        create(wrapper);
    }

    // TODO: Create path as a class that handles this?
    private String[] parsePath(CommandWrapper wrapper) {
        return wrapper.getPath().split("\\.");
    }

    // TODO: Move to a provider
    private void create(CommandWrapper wrapper) {
        commands.put(wrapper.getPath(), wrapper);

        String main = parsePath(wrapper)[0];

        // If the command has a main that is already registered, add the sub-command to it
        if (getCommand(main).isPresent()) {
            // TODO: Register sub-command to the wrapper
            return;
        }

        // TODO: This should create only the main command
        //      if a command has no children, register that instead
        Command bukkit = new Command(main) {
            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                // TODO: Logic for executing subcommands:
                //      create a map somewhere that maps the subcommands
                //      so they can be get and executed from here?
                return wrapper.execute(commandSender, s, strings);
            }
        };

        // Register command to the server map
        bukkitMap.register(main, bukkit);
    }
}
