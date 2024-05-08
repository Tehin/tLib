package dev.tehin.tlib.core.command.args;

import lombok.Getter;

import java.util.Arrays;

public class CommandPath {

    private final String[] args;

    @Getter
    private final String asString;

    public CommandPath(String path) {
        this.args = path.split("\\.");

        this.asString = path;
    }

    public boolean isSubCommand() {
        return args.length > 1;
    }

    public String getParentCommand() {
        return args[0];
    }

    public String[] getSubCommands() {
        if (!isSubCommand()) return null;

        return Arrays.copyOfRange(args, 1, args.length);
    }
}
