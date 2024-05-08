package dev.tehin.tlib.core.command.mappings;

import dev.tehin.tlib.core.command.references.NormalCommandReference;
import dev.tehin.tlib.core.command.references.NodeCommandReference;
import dev.tehin.tlib.core.command.wrapper.CommandWrapper;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.util.*;

@RequiredArgsConstructor
public class CommandMappings {

    private final CommandMap bukkit;
    private final HashMap<String, CommandWrapper> commands = new HashMap<>();
    private final HashMap<String, String> aliasesResolver = new HashMap<>();

    public Optional<CommandWrapper> get(String main) {
        String alias = aliasesResolver.get(main);

        CommandWrapper wrapper;

        if (alias != null) {
            wrapper = commands.get(alias);
        } else {
            wrapper = commands.get(main);
        }

        return Optional.ofNullable(wrapper);
    }

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
    public void load() {
        for (CommandWrapper wrapper : commands.values()) {
            String[] aliases = wrapper.getAlias();

            // TODO: Throw an error if a sub command has an alias

            if (aliases != null && aliases.length != 0) {
                Arrays.stream(aliases).forEach(alias -> aliasesResolver.put(alias, wrapper.getPath().getAsString()));
            }

            load(wrapper);
        }
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
}
