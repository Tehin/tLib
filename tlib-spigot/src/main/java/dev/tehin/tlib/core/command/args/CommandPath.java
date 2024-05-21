package dev.tehin.tlib.core.command.args;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

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

    @SneakyThrows
    public static CommandPath parse(String[] args) {
        if (args.length == 0) throw new IllegalArgumentException("No arguments on CommandPath parsing were found");

        String join = String.join(".", args);

        if (args.length == 1) join = args[0];

        return new CommandPath(join);
    }

    public String getParentCommand() {
        return args[0];
    }

    /**
     * @return The complete path, including parent command if sub-command
     */
    public String[] getCompletePath() {
        return args;
    }

    @Override
    public String toString() {
        return getAsString();
    }
}
