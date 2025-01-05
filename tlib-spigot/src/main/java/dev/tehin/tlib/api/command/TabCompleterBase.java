package dev.tehin.tlib.api.command;

import dev.tehin.tlib.core.command.args.CommandArgs;

import java.util.List;

public interface TabCompleterBase {

    List<String> complete(CommandArgs args);

}
