package dev.tehin.tlib.core.command.references;

import dev.tehin.tlib.core.command.wrapper.CommandWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

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
}
