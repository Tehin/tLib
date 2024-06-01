package dev.tehin.tlib.api.configuration.holders;

import dev.tehin.tlib.utilities.MessageUtil;
import lombok.Getter;
import org.bukkit.command.CommandSender;

@Getter
public class MessagingConfig {

    private String prefix = "";

    /**
     * What should the prefix be on {@link MessageUtil#sendWithPrefix(CommandSender, String)}
     * <br>
     * Default Value: "" (No prefix)
     */
    public MessagingConfig prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }


}
