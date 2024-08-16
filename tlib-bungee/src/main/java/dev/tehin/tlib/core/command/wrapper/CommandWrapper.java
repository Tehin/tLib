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
import net.md_5.bungee.api.CommandSender;

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

    // TODO: Move to a messaging holder?
    private String noPermissionMessage = "&cNo permission.";
    private String usage;

    // TODO: Move to an args holder?
    private Class<?>[] hardArgs;
    private int fixedLength = -1;

    private boolean loaded = false;

    public void execute(CommandSender sender, String[] args) {
        // TEMP DISABLED
//        if (!isExecutorSupported(sender)) {
//            MessageUtil.send(sender, "&cYou can't execute this command.");
//            return;
//        }

        if (!PermissionUtil.has(sender, permission)) {
            MessageUtil.send(sender, getNoPermissionMessage());
            return;
        }

        if (fixedLength != -1 && args.length != fixedLength) {
            MessageUtil.send(sender, "&cUsage: /" + getUsage());
            return;
        }

        command.execute(new CommandArgs(sender, args));
    }

    public boolean isSubCommand() {
        return path.isSubCommand();
    }

    private boolean isExecutorSupported(CommandSender sender) {
       return Arrays.stream(executors)
               .anyMatch(executor -> ReflectionUtil.isChild(executor, sender.getClass()));
    }
}
