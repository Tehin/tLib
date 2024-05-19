package dev.tehin.tlib.core.exceptions;

import dev.tehin.tlib.core.command.wrapper.CommandWrapper;

public class SubCommandWithAliasException extends Exception{

    public SubCommandWithAliasException(CommandWrapper wrapper) {
        super("Aliases on sub-commands are not supported yet (Command: " + wrapper.getPath().getAsString() + ")");
    }
}
