package dev.tehin.tlib.utilities;

import dev.tehin.tlib.utilities.task.TaskTimer;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EffectUtil {

    public static void play(Collection<Player> players, int viewingRadius, Location loc, Effect effect) {
        for (Player player : players) {
            player.spigot().playEffect(loc, effect, 0, 0, 0.0F, 0.0F, 0.0F, 1.0F, 1, viewingRadius);
        }
    }

    public static void play(Location location, int viewingRadius, Effect effect) {
        Collection<Entity> entities = location.getWorld().getNearbyEntities(location, viewingRadius, viewingRadius, viewingRadius);

        List<Player> players = entities.stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .collect(Collectors.toList());

        play(players, viewingRadius, location, effect);
    }

    public static void circle(Collection<Player> players, Location location, int viewingRadius, double radius, int count, Effect effect, double offset) {
        for (double angle = 0; angle < 360; angle += 360.0 / count) {
            double asOffset = angle + offset;
            double x = location.getX() + radius * Math.cos(Math.toRadians(asOffset));
            double z = location.getZ() + radius * Math.sin(Math.toRadians(asOffset));
            Location particleLocation = new Location(location.getWorld(), x, location.getY(), z);

            play(players, viewingRadius, particleLocation, effect);
        }
    }

    public static void wave(Plugin plugin, Collection<Player> players, Location loc, Effect effect, int viewingRadius, double radius, int durationOnTicks, boolean inverted) {
        Consumer<TaskTimer> task = (timer) -> {
            double counter = (inverted) ? radius - timer.getCounter() : timer.getCounter();
            circle(players, loc, viewingRadius, counter, 18, effect, 0);
        };

        double increment = radius / durationOnTicks;
        new TaskTimer(task, TaskTimer.EMPTY_RUNNABLE, durationOnTicks, increment, 50, true)
                .runSync(plugin);
    }

}
