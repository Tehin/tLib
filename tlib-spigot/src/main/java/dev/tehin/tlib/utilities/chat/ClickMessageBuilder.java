package dev.tehin.tlib.utilities.chat;

import dev.tehin.tlib.utilities.MessageUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class ClickMessageBuilder {

    private final String message, hover, command;

    private TextComponent build() {
        TextComponent component = new TextComponent(TextComponent.fromLegacyText(MessageUtil.color(message)));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessageUtil.color(hover)).create()));

        return component;
    }

    public void send(Player player) {
        MessageUtil.send(player, build());
    }
}
