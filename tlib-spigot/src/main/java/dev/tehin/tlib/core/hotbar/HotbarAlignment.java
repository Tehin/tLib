package dev.tehin.tlib.core.hotbar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HotbarAlignment {

    LEFT(-1),
    RIGHT(-1),
    SLOT_1(1),
    SLOT_2(2),
    SLOT_3(3),
    SLOT_4(4),
    SLOT_5(5),
    SLOT_6(6),
    SLOT_7(7),
    SLOT_8(8),
    SLOT_9(9);

    private final int slot;

    public static HotbarAlignment fromSlot(int slot) {
        for (HotbarAlignment alignment : HotbarAlignment.values()) {
            if (alignment.getSlot() == slot) return alignment;
        }

        return LEFT;
    }

}
