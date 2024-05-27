package dev.tehin.tlib.utilities.effects;

import lombok.*;
import lombok.experimental.Accessors;
import org.bukkit.Effect;

@Accessors(fluent = true, chain = true)
@Setter
@RequiredArgsConstructor
public class EffectBuilder {

    private final Effect effect;

    private int viewingRadius = 20;

    private int data = 0;

    private int particlesPerEffect = 1;
    private int particlesPerPacket = 1;

    private float speed = 1f;
    private RGB rgb = new RGB(0, 0, 0);

    public Effect getEffect() {
        return effect;
    }

    public int getViewingRadius() {
        return viewingRadius;
    }

    public int getData() {
        return data;
    }

    public int getParticlesPerEffect() {
        return particlesPerEffect;
    }

    public int getParticlesPerPacket() {
        return particlesPerPacket;
    }

    public float getSpeed() {
        return speed;
    }

    public RGB getRgb() {
        return rgb;
    }
}
