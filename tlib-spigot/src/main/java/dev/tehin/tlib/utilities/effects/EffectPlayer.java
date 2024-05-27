package dev.tehin.tlib.utilities.effects;

import dev.tehin.tlib.utilities.task.TaskTimer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EffectPlayer {

    private final EffectBuilder builder;

    public void normal(Location location) {
        final double viewing = builder.getViewingRadius();

        Collection<Entity> entities = location.getWorld().getNearbyEntities(location, viewing, viewing, viewing);

        List<Player> players = entities.stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .collect(Collectors.toList());

        RGB rgb = builder.getRgb();

        for (Player player : players) {
            player.spigot().playEffect(location,
                    builder.getEffect(),
                    0,
                    builder.getData(),
                    rgb.getR() / 255, rgb.getG() / 255, rgb.getB() / 255,
                    builder.getSpeed(),
                    builder.getParticlesPerPacket(),
                    builder.getViewingRadius()
            );
        }
    }

    public void circle(Location location, double radius, double angle) {
        for (double currentAngle = 0; currentAngle < 360; currentAngle += 360.0 / builder.getParticlesPerEffect()) {
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
