package dev.tehin.tlib.core.command.executors;

import dev.tehin.tlib.api.command.CommandBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class SimpleCommandExecutor implements CommandBase {

    private final String path;
    private final String permission;

}
