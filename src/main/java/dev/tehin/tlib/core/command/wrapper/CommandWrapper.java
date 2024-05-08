package dev.tehin.tlib.core.command.wrapper;

import dev.tehin.tlib.api.command.CommandBase;
import dev.tehin.tlib.core.command.args.CommandArgs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
@Getter
@Setter
public class CommandWrapper {

    private final CommandBase command;
    private final Class<? extends CommandSender>[] executors;
    private final String path;

    private String description, permission;
    private String[] alias;
    private Class<?>[] hardArgs;


    public boolean execute(CommandSender sender, String alias, String[] args) {
        command.execute(new CommandArgs(sender, alias, args));

        return true;
    }
}
