package dev.tehin.tlib.utilities.task;

import dev.tehin.tlib.api.tLib;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.function.Supplier;

public class TaskSet {

    // Sets that use queues for overlap prevention
    private static final Map<UUID, List<TaskSet>> QUEUES = new HashMap<>();

    private final Map<Integer, TaskEntry> tasks = new HashMap<>();

    private BukkitTask task = null;
    private int duration = 0;

    private Supplier<Boolean> cancelIf;
    private Runnable onCancel;

    public static void stop(Player player) {
        List<TaskSet> set = QUEUES.remove(player.getUniqueId());
        if (set == null) return;

        set.forEach(TaskSet::cancel);
    }

    private void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public TaskSet at(int time, Runnable task) {
        tasks.put(time, new TaskEntry(time, task));
        duration += time;
        return this;
    }

    public TaskSet onCancel(Runnable task) {
        this.onCancel = task;
        return this;
    }

    public TaskSet cancelIf(Supplier<Boolean> condition) {
        this.cancelIf = condition;
        return this;
    }

    public int getTotalDuration() {
        return duration;
    }

    // TODO: Tomar en cuenta si las tareas se dan en diferentes timings (reducir la duration conforme pasan los ticks)
    public void queueRun(Player player, int endOffset) {
        Runnable task = this::runSync;

        int playerDelay = 0;

        if (QUEUES.containsKey(player.getUniqueId())) {
            playerDelay = QUEUES.get(player.getUniqueId()).stream().mapToInt(TaskSet::getTotalDuration).sum();
        }

        // TODO: Implement the end offset
        TaskUtil.runSyncLater(task, playerDelay);
    }

    public void runSync() {
        this.task = createTask().runTaskTimer(tLib.get().getOwner(), 0, 1);
    }

    public void runAsync() {
        this.task = createTask().runTaskTimerAsynchronously(tLib.get().getOwner(), 0, 1);
    }

    private BukkitRunnable createTask() {
        return new BukkitRunnable() {

            private int currentTick = 0;

            @Override
            public void run() {
                if (currentTick > duration) {
                    this.cancel();
                    return;
                }

                TaskEntry entry = tasks.get(currentTick++);
                if (entry == null) return;

                injectCancelOnRunnable(entry.task()).run();
            }
        };
    }

    private Runnable injectCancelOnRunnable(Runnable entry) {
        return () -> {
            if (cancelIf != null && cancelIf.get()) {
                if (onCancel != null) {
                    onCancel.run();
                    onCancel = null;

                    this.task.cancel();
                }
                return;
            }

            entry.run();
        };
    }

}
