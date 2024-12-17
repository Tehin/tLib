package dev.tehin.tlib.core.menu.options;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minemora.nms.NMS;
import org.bukkit.Sound;

@RequiredArgsConstructor
@Accessors(fluent = true, chain = true)
@Setter
@Getter
public class MenuOptions {

    private Sound soundOnOpen = NMS.Sound.ITEM_PICKUP;
    private Sound soundOnClick = NMS.Sound.CLICK;


}
