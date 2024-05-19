package dev.tehin.tlib.core.command.references;

import dev.tehin.tlib.core.command.args.CommandPath;
import dev.tehin.tlib.core.command.wrapper.CommandWrapper;
import dev.tehin.tlib.utilities.AlgorithmicUtil;
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
        Optional<CommandWrapper> match = match(CommandPath.parse(args));

        // Prevent NPEs from giving error messages if sub-command not found
        // TODO: Replace with parent command help message
        if (!match.isPresent()) {
            MessageUtil.send(sender, "&cSpecified sub-command was not found, please use '/help'");
            return false;
        }


        // Send to the sub-command only its arguments, ignoring the sub-command path
        int length = match.get().getPath().getSubCommands().length;

        String[] parsedArgs = Arrays.copyOfRange(args, length, args.length);

        match.get().execute(sender, label, parsedArgs);
        return true;
    }

    // TODO: Improve performance if really needed?
    private Optional<CommandWrapper> match(CommandPath arg) {
        if (arg.getSubCommands() == null) return Optional.empty();

        /*
         * We traverse all the nodes, in order to find the best match
         *
         * If there are more than one option with the match rate, the first
         * one found will be returned
         *
         * This is done due to the fact that sub-commands have arguments, which
         * are NOT included in the sub-command path
         */
        String bestMatch = AlgorithmicUtil.getBestMatch(nodes.keySet(), "\\.", arg.getSubCommands());

        return Optional.ofNullable(nodes.get(bestMatch));
    }
}
