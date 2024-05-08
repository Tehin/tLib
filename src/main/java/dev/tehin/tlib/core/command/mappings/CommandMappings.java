package dev.tehin.tlib.core.command.mappings;

import dev.tehin.tlib.core.command.wrapper.CommandWrapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class CommandMappings {

    private final HashMap<String, CommandWrapper> commands = new HashMap<>();
    private final HashMap<String, String> aliasesResolver = new HashMap<>();

    public Optional<CommandWrapper> get(String main) {
        String alias = aliasesResolver.get(main);

        CommandWrapper wrapper;

        if (alias != null) {
            wrapper = commands.get(alias);
        } else {
            wrapper = commands.get(main);
        }

        return Optional.ofNullable(wrapper);
    }

    public void register(CommandWrapper wrapper) {
        commands.put(wrapper.getPath().getAsString(), wrapper);

        String[] aliases = wrapper.getAlias();
        if (aliases != null && aliases.length != 0) {
            Arrays.stream(aliases).forEach(alias -> aliasesResolver.put(alias, wrapper.getPath().getAsString()));
        }
    }
}
