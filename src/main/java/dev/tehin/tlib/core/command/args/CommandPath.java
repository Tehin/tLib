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

        this.setSubCommand(args.length > 0);

        this.asString = path.toLowerCase(Locale.ROOT);

        if (isSubCommand()) subCommands = Arrays.copyOfRange(args, 1, args.length);
    }

    public static CommandPath parse(String[] args) {
        String join = String.join(".", args);

        return new CommandPath(join);
    }

    public String getParentCommand() {
        return args[0];
    }
}
