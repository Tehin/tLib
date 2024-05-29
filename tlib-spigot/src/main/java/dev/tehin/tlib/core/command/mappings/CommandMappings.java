package dev.tehin.tlib.core.command.mappings;

import dev.tehin.tlib.core.command.references.NormalCommandReference;
import dev.tehin.tlib.core.command.references.NodeCommandReference;
import dev.tehin.tlib.core.command.wrapper.CommandWrapper;
import dev.tehin.tlib.core.exceptions.SubCommandWithAliasException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import java.util.*;

@RequiredArgsConstructor
public class CommandMappings {

    private final CommandMap bukkit;
    private final HashMap<String, CommandWrapper> commands = new HashMap<>();

    public void register(CommandWrapper wrapper) {
        commands.put(wrapper.getPath().getAsString(), wrapper);
    }

    /*
     * Load the commands after each of them is registered
     * to ensure a clean sub command mappings
     *
     * This removes the need for a specific map containing every sub-command
     * since they are loaded inside the Bukkit command
     */
    @SneakyThrows
    public void load() {
        for (CommandWrapper wrapper : commands.values()) {
            String[] aliases = wrapper.getAlias();

            // Prevent sub commands from having aliases
            if (aliases != null && aliases.length > 0 && wrapper.isSubCommand()) {
                throw new SubCommandWithAliasException(wrapper);
            }

            load(wrapper);
        }

        setCompletions();
    }

    // TODO: Move to a command load provider?
    private void load(CommandWrapper wrapper) {
        String main = wrapper.getPath().getParentCommand();

        Command bukkit;

        if (wrapper.isSubCommand()) {
            bukkit = new NodeCommandReference(main, getSubCommands(main));
        } else {
            bukkit = new NormalCommandReference(wrapper);
        }

        // Register command to the server map
        this.bukkit.register(main, bukkit);
    }

    // TODO: Move to a command load provider?
    private List<CommandWrapper> getSubCommands(String main) {
        List<CommandWrapper> subs = new ArrayList<>();

        for (CommandWrapper value : commands.values()) {
            if (!value.isSubCommand()) continue;

            if (!value.getPath().getParentCommand().equals(main)) continue;

            subs.add(value);
        }

        return subs;
    }
    
    // TODO: Move to a command loader provider?
    private void setCompletions() {
        for (CommandWrapper value : commands.values()) {
            TabCompleter completer = value.getTabCompleter();
            if (completer == null) continue;

            String parent = value.getPath().getParentCommand();

            PluginCommand bukkit = Bukkit.getPluginCommand(parent);
            if (bukkit == null) throw new IllegalStateException("Command /" + parent + " was not found in Bukkit list");

            bukkit.setTabCompleter(completer);
        }
    }
}
