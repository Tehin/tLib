package dev.tehin.tlib.utilities.effects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Effect;

@Accessors(fluent = true, chain = true)
@Setter
@Getter
@RequiredArgsConstructor
public class EffectBuilder {

    private final Effect effect;

    private int viewingRadius = 20;

    private int data = 0;

    private int particlesPerEffect = 1;
    private int particlesPerPacket = 1;

    private float speed = 1f;
    private RGB rgb = new RGB(0, 0, 0);

}
