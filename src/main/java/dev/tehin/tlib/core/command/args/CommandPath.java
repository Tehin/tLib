package dev.tehin.tlib.core.command.args;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

public class CommandPath {

    private final String[] args;

    @Getter
    private final String asString;

    @Getter
    private final String[] subCommands;

    public CommandPath(String path) {
        this.args = path.split("\\.");

        this.asString = path.toLowerCase(Locale.ROOT);

        if (isSubCommand()) subCommands = Arrays.copyOfRange(args, 1, args.length);
        else subCommands = null;
    }

    public static CommandPath parse(String[] args) {
        String join = String.join(".", args);

        return new CommandPath(join);
    }

    public boolean isSubCommand() {
        return args.length > 1;
    }

    public String getParentCommand() {
        return args[0];
    }
}
