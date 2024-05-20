package dev.tehin.tlib.api.configuration;

import dev.tehin.tlib.api.configuration.holders.CommandsConfig;
import dev.tehin.tlib.api.configuration.holders.MenusConfig;
import dev.tehin.tlib.api.configuration.holders.TasksConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true, chain = true)
@Setter
@Getter
public class LibConfiguration {

    private CommandsConfig commands = new CommandsConfig();
    private MenusConfig menus = new MenusConfig();
    private TasksConfig tasks = new TasksConfig();

}
