package dev.tehin.tlib.core.command.wrapper;

import dev.tehin.tlib.api.command.CommandBase;
import dev.tehin.tlib.core.command.args.CommandArgs;
import dev.tehin.tlib.core.command.args.CommandPath;
import dev.tehin.tlib.utilities.MessageUtil;
import dev.tehin.tlib.utilities.PermissionUtil;
import dev.tehin.tlib.utilities.ReflectionUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
@Setter
public class CommandWrapper {

    private final CommandBase command;
    private final Class<? extends CommandSender>[] executors;
    private final String permission;
    private final CommandPath path;

    private String description = "";
    private String[] alias = new String[0];

    private TabCompleter tabCompleter;

    // TODO: Move to a messaging holder?
    private String noPermissionMessage = PermissionUtil.getDefaultMessage();
    private String usage;

    // TODO: Move to an args holder?
    private Class<?>[] hardArgs;
    private int fixedLength = -1;

    private boolean loaded = false;

    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isExecutorSupported(sender)) {
            MessageUtil.send(sender, "&cYou can't execute this command.");
            return false;
        }

        if (!PermissionUtil.has(sender, permission)) {
            MessageUtil.send(sender, getNoPermissionMessage());
            return false;
        }

        if (fixedLength != -1 && args.length != fixedLength) {
            MessageUtil.send(sender, "&cUsage: /" + getUsage());
            return false;
        }

        command.execute(new CommandArgs(sender, alias, args));

        return true;
    }

    public boolean isSubCommand() {
        return path.isSubCommand();
    }

    private boolean isExecutorSupported(CommandSender sender) {
       return Arrays.stream(executors)
               .anyMatch(executor -> ReflectionUtil.isChild(executor, sender.getClass()));
    }
}
