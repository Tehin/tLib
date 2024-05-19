package dev.tehin.tlib.api.command;

import dev.tehin.tlib.core.command.args.CommandArgs;

public interface CommandBase {

    /**
     * Called when the command is executed by the sender
     * @param args Arguments from the command
     */
    void execute(CommandArgs args);
}
