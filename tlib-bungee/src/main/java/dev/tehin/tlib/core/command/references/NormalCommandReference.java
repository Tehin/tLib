package dev.tehin.tlib.core.command.references;

import dev.tehin.tlib.core.command.wrapper.CommandWrapper;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class NormalCommandReference extends Command {

    private final CommandWrapper wrapper;

    public NormalCommandReference(CommandWrapper wrapper) {
        super(wrapper.getPath().getParentCommand(), wrapper.getPermission(), wrapper.getAlias());

        this.wrapper = wrapper;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        wrapper.execute(sender, args);
    }
}
