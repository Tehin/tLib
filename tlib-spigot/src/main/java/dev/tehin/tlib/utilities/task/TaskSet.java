package dev.tehin.tlib.utilities.task;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Supplier;

public class TaskSet {

    // Sets that use queues for overlap prevention
    @Getter
    private static final Map<UUID, Integer> queues = new HashMap<>();

    private final List<TaskEntry> tasks = new LinkedList<>();

    private Supplier<Boolean> cancelIf;
    private Runnable onCancel;

    public TaskSet at(int time, Runnable task) {
        tasks.add(new TaskEntry(time, task));
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
        return tasks.stream().mapToInt(TaskEntry::delay).sum();
    }

    // TODO: Tomar en cuenta si las tareas se dan en diferentes timings (reducir la duration conforme pasan los ticks)
    public void queueRun(Player player, int endOffset) {
        int totalDuration = getTotalDuration() + endOffset + 1;

        Runnable task = () -> {
            runSync();

            // Clear this set from the queue after it's finished
            TaskUtil.runSyncLater(() -> {
                queues.computeIfPresent(player.getUniqueId(), (k, v) -> {
                    int result = v - totalDuration;
                    if (result <= 0) return null;

                    return result;
                });
            }, totalDuration);
        };

        TaskUtil.runSyncLater(task, queues.getOrDefault(player.getUniqueId(), 0));

        queues.compute(player.getUniqueId(), (k, v) -> {
            if (v == null) v = 0;

            return v + totalDuration;
        });
    }

    public void runSync() {
        for (TaskEntry entry : tasks) {
            schedule(entry, false);
        }
    }

    public void runAsync() {
        for (TaskEntry task : tasks) {
            schedule(task, true);
        }
    }

    private Runnable getRunnable(Runnable entry) {
        return () -> {
            if (cancelIf != null && cancelIf.get()) {
                if (onCancel != null) {
                    onCancel.run();
                    onCancel = null;
                }
                return;
            }

            entry.run();
        };
    }

    private void schedule(TaskEntry entry, boolean async) {
        Runnable task = getRunnable(entry.task());

        if (entry.delay() == 0) {
            if (async) {
                TaskUtil.runAsync(task);
            } else {
                TaskUtil.runSync(task);
            }

            return;
        }

        if (async) {
            TaskUtil.runAsyncLater(getRunnable(entry.task()), entry.delay());
        } else {
            TaskUtil.runSyncLater(getRunnable(entry.task()), entry.delay());
        }
    }

}
