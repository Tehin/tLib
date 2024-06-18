package dev.tehin.tlib.api.configuration;

import dev.tehin.tlib.api.configuration.holders.CommandsConfig;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true, chain = true)
@Setter
public class LibConfiguration {

    private CommandsConfig commands;

}
