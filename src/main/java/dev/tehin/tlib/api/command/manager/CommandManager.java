package dev.tehin.tlib.api.command.manager;

import dev.tehin.tlib.api.command.CommandBase;

public interface CommandManager {
    void register(CommandBase[] command);
}
