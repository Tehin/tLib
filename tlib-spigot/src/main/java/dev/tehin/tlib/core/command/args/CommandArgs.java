package dev.tehin.tlib.core.command.args;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

@Getter
@AllArgsConstructor
public class CommandArgs {

    private final CommandSender sender;
    private final String alias;
    private final String[] args;

    public String getArg(int index) {
        return args[index];
    }

    public int getArgsLength() {
        return args.length;
    }

}
