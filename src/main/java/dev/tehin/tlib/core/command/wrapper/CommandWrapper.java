package dev.tehin.tlib.core.command.wrapper;

import dev.tehin.tlib.api.command.CommandBase;
import dev.tehin.tlib.core.command.args.CommandArgs;
import dev.tehin.tlib.core.command.args.CommandPath;
import dev.tehin.tlib.utilities.MessageUtil;
import dev.tehin.tlib.utilities.PermissionUtil;
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
    private final String permission;
    private final CommandPath path;

    private String description = "";
    private String[] alias = new String[0];
    private Class<?>[] hardArgs;

    private boolean loaded = false;

    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!PermissionUtil.has(sender, permission)) {
            PermissionUtil.sendMessage(sender);
            return true;
        }

        command.execute(new CommandArgs(sender, alias, args));

        return true;
    }

    public boolean isSubCommand() {
        return path.isSubCommand();
    }
}
