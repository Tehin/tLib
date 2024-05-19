package dev.tehin.tlib.core.command.mappings;

import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.command.references.NormalCommandReference;
import dev.tehin.tlib.core.command.references.NodeCommandReference;
import dev.tehin.tlib.core.command.wrapper.CommandWrapper;
import dev.tehin.tlib.core.exceptions.SubCommandWithAliasException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;

@RequiredArgsConstructor
public class CommandMappings {

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
    }

    // TODO: Move to a command load provider?
    private void load(CommandWrapper wrapper) {
        String main = wrapper.getPath().getParentCommand();

        Command command;

        if (wrapper.isSubCommand()) {
            command = new NodeCommandReference(main, getSubCommands(main));
        } else {
            command = new NormalCommandReference(wrapper);
        }

        // Register command to the server map
        ProxyServer.getInstance().getPluginManager().registerCommand(tLib.get().getOwner(), command);
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
