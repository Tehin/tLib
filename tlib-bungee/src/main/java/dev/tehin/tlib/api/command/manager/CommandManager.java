package dev.tehin.tlib.api.command.manager;

import dev.tehin.tlib.api.command.CommandBase;

public interface CommandManager {

    /**
     * Registers and loads the commands
     * @param commands Commands to be registered
     */
    void register(CommandBase... commands);
}
