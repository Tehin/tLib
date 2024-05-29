package dev.tehin.tlib.core.command.references;

import dev.tehin.tlib.api.command.TabCompleterBase;
import dev.tehin.tlib.core.command.wrapper.CommandWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NormalCommandReference extends Command {

    private final CommandWrapper wrapper;

    public NormalCommandReference(CommandWrapper wrapper) {
        super(wrapper.getPath().getParentCommand());

        setAliases(Arrays.asList(wrapper.getAlias()));
        setDescription(wrapper.getDescription());

        this.wrapper = wrapper;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        wrapper.execute(sender, label, args);

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (!(wrapper.getCommand() instanceof TabCompleterBase)) return super.tabComplete(sender, alias, args);

        TabCompleterBase completer = (TabCompleterBase) wrapper.getCommand();
        return completer.complete(args);
    }
}
