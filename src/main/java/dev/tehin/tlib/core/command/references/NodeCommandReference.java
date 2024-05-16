package dev.tehin.tlib.core.command.references;

import dev.tehin.tlib.core.command.args.CommandPath;
import dev.tehin.tlib.core.command.wrapper.CommandWrapper;
import dev.tehin.tlib.utilities.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;

public class NodeCommandReference extends Command {

    private final Map<String, CommandWrapper> nodes = new HashMap<>();

    public NodeCommandReference(String name, List<CommandWrapper> subCommands) {
        super(name);

        for (CommandWrapper subCommand : subCommands) {
            nodes.put(subCommand.getPath().getAsString(), subCommand);
        }
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        CommandWrapper match = match(CommandPath.parse(args));

        // Prevent NPEs from giving error messages if sub-command not found
        // TODO: Replace with parent command help message
        if (match == null) {
            MessageUtil.send(sender, "&cSpecified sub-command was not found, please use '/help'");
            return false;
        }

        // Send to the sub-command only its arguments, ignoring the sub-command path
        int length = match.getPath().getSubCommands().length;

        String[] parsedArgs = Arrays.copyOfRange(args, length, args.length);

        match.execute(sender, label, parsedArgs);
        return true;
    }

    // TODO: Improve performance if really needed?
    private CommandWrapper match(CommandPath path) {
        String best = null;

        /*
         * We traverse all the nodes, checking if the key contains a part
         * of the required path
         *
         * We return the longest key found since it will always
         * be the correct one
         *
         * This is done due to the fact that sub-commands have arguments, which
         * are NOT included in the sub-command path
         */
        for (String key : nodes.keySet()) {
            if (!key.contains(path.getAsString())) continue;

            if (best == null) {
                best = key;
                continue;
            }

            if (best.length() >= key.length()) continue;

            best = key;
        }

        return nodes.get(best);
    }
}
