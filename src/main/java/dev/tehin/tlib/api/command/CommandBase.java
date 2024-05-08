package dev.tehin.tlib.api.command;

import dev.tehin.tlib.core.command.args.CommandArgs;

public interface CommandBase {
    void execute(CommandArgs args);
}
