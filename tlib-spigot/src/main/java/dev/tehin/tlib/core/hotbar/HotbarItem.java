package dev.tehin.tlib.core.hotbar;

import dev.tehin.tlib.api.hotbar.HotbarItemCondition;
import dev.tehin.tlib.core.item.InteractableItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class HotbarItem {

    private final String id;
    private final HotbarAlignment alignment;
    private final InteractableItem interactable;
    private final HotbarItemCondition condition;

}
