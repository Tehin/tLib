package dev.tehin.tlib.core.command.args;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public class CommandPath {

    @Getter(AccessLevel.NONE)
    private final String[] args;

    private final String asString;

    private String[] subCommands = null;

    private @Setter boolean subCommand = false;

    public CommandPath(String path) {
        this.args = path.split("\\.");
        this.asString = path.toLowerCase(Locale.ROOT);

        // First 'path' argument is the parent command, the args should be
        // greater than one to be taken as a sub-command
        this.setSubCommand(!path.isEmpty() && args.length > 1);

        if (isSubCommand()) subCommands = Arrays.copyOfRange(args, 1, args.length);
    }

    public static CommandPath parse(String[] args) {
        String join = String.join(".", args);

        return new CommandPath(join);
    }

    public String getParentCommand() {
        return args[0];
    }

    @Override
    public String toString() {
        return getAsString();
    }
}
