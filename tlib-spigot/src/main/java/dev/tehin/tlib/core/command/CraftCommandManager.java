package dev.tehin.tlib.core.command;

import dev.tehin.tlib.api.command.annotation.*;
import dev.tehin.tlib.api.command.manager.CommandManager;
import dev.tehin.tlib.api.command.CommandBase;
import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.command.args.CommandPath;
import dev.tehin.tlib.core.command.executors.SimpleCommandExecutor;
import dev.tehin.tlib.core.command.mappings.CommandMappings;
import dev.tehin.tlib.core.command.wrapper.CommandWrapper;
import dev.tehin.tlib.core.exceptions.CommandsAlreadyRegisteredException;
import dev.tehin.tlib.core.exceptions.NoPropertiesFoundException;
import dev.tehin.tlib.utilities.PermissionUtil;
import lombok.SneakyThrows;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CraftCommandManager implements CommandManager {

    private CommandMappings commands;
    private boolean loaded = false;

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

    @Override
    public void register(CommandBase... commands) {
        Arrays.stream(commands).forEach(this::register);
    }

    @SneakyThrows
    @Override
    public void load() {
        if (this.loaded) throw new CommandsAlreadyRegisteredException();

        this.loaded = true;
        this.commands.load();
    }

    @SneakyThrows
    private void register(CommandBase command) {
        boolean isSimple = command instanceof SimpleCommandExecutor;
        if (isSimple) {
            registerSimple((SimpleCommandExecutor) command);
            return;
        }

        CommandProperties properties = command.getClass().getAnnotation(CommandProperties.class);
        if (properties == null) throw new NoPropertiesFoundException(CommandBase.class);

        // These are not required
        CommandAliases aliases = command.getClass().getAnnotation(CommandAliases.class);
        CommandDescription description = command.getClass().getAnnotation(CommandDescription.class);
        CommandArgsStructure args = command.getClass().getAnnotation(CommandArgsStructure.class);
        CommandMessaging messaging = command.getClass().getAnnotation(CommandMessaging.class);

        CommandWrapper wrapper = new CommandWrapper(command, properties.executors(), properties.permission(), new CommandPath(properties.path()));

        if (aliases != null) wrapper.setAlias(aliases.value());

        if (description != null) wrapper.setDescription(description.value());

        if (args != null) {
            wrapper.setHardArgs(args.structure());
            wrapper.setFixedLength(args.fixedLength());
        }

        if (messaging != null) {
            wrapper.setUsage(messaging.usage());

            if (messaging.noPermission().equalsIgnoreCase("hide")) {
                wrapper.setNoPermissionMessage(PermissionUtil.getBukkitHelpMessage());
            } else {
                wrapper.setNoPermissionMessage(messaging.noPermission());
            }
        }

        commands.register(wrapper);
    }

    private void registerSimple(SimpleCommandExecutor command) {
        CommandWrapper wrapper = new CommandWrapper(command, null, command.getPermission(), new CommandPath(command.getPath()));

        commands.register(wrapper);
    }

}
