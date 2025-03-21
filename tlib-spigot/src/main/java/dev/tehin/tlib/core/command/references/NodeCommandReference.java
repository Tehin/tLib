package dev.tehin.tlib.core.command.references;

import dev.tehin.tlib.core.command.args.CommandPath;
import dev.tehin.tlib.core.command.wrapper.CommandWrapper;
import dev.tehin.tlib.utilities.AlgorithmicUtil;
import dev.tehin.tlib.utilities.MessageUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;

public class NodeCommandReference extends Command {

    private final Map<String, CommandWrapper> nodes = new HashMap<>();

    public NodeCommandReference(String name, List<CommandWrapper> subCommands) {
        super(name);

        for (CommandWrapper subCommand : subCommands) {
            /*
             * Replace the parent command from our nodes
             * since we base our search in the sub-commands provided.
             *
             * We need to ignore the parent command on search to
             * prevent unwanted results (nodes comparing to parent, etc.)
             */
            String asString = subCommand.getPath()
                    .getAsString()
                    .replaceFirst(name + ".", "");

            nodes.put(asString, subCommand);
        }
    }

    private void sendHelp(CommandSender sender) {
        if (nodes.containsKey("help")) {
            nodes.get("help").execute(sender, "", new String[0]);
            return;
        }


        MessageUtil.send(sender, "&cSpecified sub-command was not found, please use '/help'");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] bukkitArgs) {
        // Require at least a sub-command for executing, if not, send help message
        if (bukkitArgs.length == 0) {
            sendHelp(sender);
            return false;
        }

        /*
         * Add the parent command to the path
         *
         * This is done so the CommandPath always contains the whole
         * path, not only arguments of the sub-command
         *
         * Even if we only use the sub-commands for searching, the
         * parent command needs to be inside the CommandPath for
         * structure purposes
         */
        String[] args = {label};
        args = ArrayUtils.addAll(args, bukkitArgs);

        Optional<CommandWrapper> match = match(CommandPath.parse(args));

        // Prevent NPEs from giving error messages if sub-command not found
        if (!match.isPresent()) {
            sendHelp(sender);
            return false;
        }

        // Send to the sub-command only its arguments, ignoring the sub-command path
        // add one since the start is inclusive, and we don't need the sub command
        int startIndex = match.get().getPath().getSubCommands().length + 1;

        if (startIndex > args.length) {
            sendHelp(sender);
            return false;
        }

        String[] parsedArgs = Arrays.copyOfRange(args, startIndex, args.length);

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
        Optional<String> best = AlgorithmicUtil.getBestMatch(nodes.keySet(), "\\.", arg.getSubCommands());
        return best.map(nodes::get);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return super.tabComplete(sender, alias, args);

        // TODO: Add sub-command completion based on permissions?
    }
}
