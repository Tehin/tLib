package dev.tehin.tlib.core.command;

import org.bukkit.command.CommandSender;

public abstract class CommandBase {

    public abstract void execute(CommandSender sender, String alias, String args);

}
