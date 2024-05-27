package dev.tehin.tlib.utilities.effects;

import dev.tehin.tlib.utilities.task.TaskTimer;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    public void normal(Location location) {
        Collection<Entity> entities = location.getWorld().getNearbyEntities(location, viewingRadius, viewingRadius, viewingRadius);

        List<Player> players = entities.stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .collect(Collectors.toList());

        for (Player player : players) {
            player.spigot().playEffect(location,
                    effect,
                    0,
                    data,
                    rgb.getR() / 255, rgb.getG() / 255, rgb.getB() / 255,
                    speed,
                    particlesPerPacket,
                    viewingRadius
            );
        }
    }

    public void circle(Location location, double radius, double angle) {
        for (double currentAngle = 0; currentAngle < 360; currentAngle += 360.0 / particlesPerEffect) {
            double asOffset = currentAngle + angle;
            double x = location.getX() + radius * Math.cos(Math.toRadians(asOffset));
            double z = location.getZ() + radius * Math.sin(Math.toRadians(asOffset));
            Location particleLocation = new Location(location.getWorld(), x, location.getY(), z);

            normal(particleLocation);
        }
    }

    public void wave(Location location, double radius, double duration) {
        Consumer<TaskTimer> task = (timer) -> circle(location, (int) timer.getCounter(), 0);

        double increment = radius / duration;
        new TaskTimer(task, TaskTimer.EMPTY_RUNNABLE, duration, increment, 50, true)
                .runAsync();
    }

}
