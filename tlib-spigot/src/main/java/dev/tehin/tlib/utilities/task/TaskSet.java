package dev.tehin.tlib.utilities.task;

import dev.tehin.tlib.api.tLib;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.function.Supplier;

public class TaskSet {

    /*
     * Sets that use queues for overlap prevention
     *
     * Use a set to prevent queue tasks from registering twice due to the
     * set being added to the queue before calling an runSync()
     */
    private static final Map<UUID, Set<TaskSet>> QUEUES = new HashMap<>();

    // Task per ticks
    private final Map<Integer, TaskEntry> tasks = new HashMap<>();

    private BukkitTask task = null;

    private Supplier<Boolean> cancelIf;
    private Runnable onCancel;

    // The total duration
    @Getter
    private int totalDuration = 0;

    // Ticks left for the task to end
    @Getter
    private int timeLeft = 0;

    // The player, if it has one assigned (only queues)
    private UUID player;

    public static void stopAll(Player player) {
        Set<TaskSet> set = QUEUES.remove(player.getUniqueId());
        if (set == null) return;

        set.forEach(TaskSet::end);
    }

    // Does nothing for the time being
    public TaskSet addPause(int pause) {
        return withDelay(pause, () -> {});
    }

    // Extra delay after the task end, so we ensure
    // the other task does not start too fast
    public TaskSet endOffset(int offset) {
        setTotalDuration(this.totalDuration + offset);
        return this;
    }

    // A task that runs at a specific time
    public TaskSet at(int time, Runnable task) {
        tasks.put(time, new TaskEntry(time, task));
        setTotalDuration(time);
        return this;
    }

    // Sets the total duration
    private void setTotalDuration(int time) {
        this.totalDuration = time;
        this.timeLeft = time;
    }

    // A task that runs N ticks after the last one
    public TaskSet withDelay(int delay, Runnable task) {
        int time = totalDuration + delay;
        at(time, task);
        return this;
    }

    // The runnable that runs when the task is cancelled
    public TaskSet onCancel(Runnable task) {
        this.onCancel = task;
        return this;
    }

    // Cancel condition
    public TaskSet cancelIf(Supplier<Boolean> condition) {
        this.cancelIf = condition;
        return this;
    }

    public void queue(Player player) {
        this.player = player.getUniqueId();

        Runnable task = this::runSync;

        int startWait = 0;

        Set<TaskSet> tasksInQueue = QUEUES.get(player.getUniqueId());
        if (tasksInQueue != null) {
            for (TaskSet taskSet : tasksInQueue) {
                startWait += taskSet.getTimeLeft();
            }
        }

        TaskUtil.runSyncLater(task, startWait);
        onTaskCreate();
    }

    public void runSync() {
        this.task = createTask().runTaskTimer(tLib.get().getOwner(), 0, 1);
        onTaskCreate();
    }

    public void runAsync() {
        this.task = createTask().runTaskTimerAsynchronously(tLib.get().getOwner(), 0, 1);
        onTaskCreate();
    }

    private BukkitRunnable createTask() {
        return new BukkitRunnable() {

            private int currentTick = 0;

            @Override
            public void run() {
                if (timeLeft < 0) {
                    end();
                    return;
                }

                TaskEntry entry = tasks.get(currentTick++);
                timeLeft--;

                if (entry == null) return;

                injectCancelOnRunnable(entry.task()).run();
            }
        };
    }

    private void onTaskCreate() {
        if (player == null) return;

        QUEUES.compute(player, (uuid, list) -> {
            if (list == null) {
                list = new HashSet<>();
            }

            list.add(this);
            return list;
        });
    }

    private void end() {
        this.task.cancel();
        this.task = null;

        if (player == null) return;

        QUEUES.get(player).remove(this);
    }

    private Runnable injectCancelOnRunnable(Runnable entry) {
        return () -> {
            if (cancelIf != null && cancelIf.get()) {
                if (onCancel != null) {
                    onCancel.run();
                    onCancel = null;

                    end();
                }
                return;
            }

            entry.run();
        };
    }

}
