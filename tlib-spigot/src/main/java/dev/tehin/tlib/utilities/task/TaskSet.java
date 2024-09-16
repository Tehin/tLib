package dev.tehin.tlib.utilities.task;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class TaskSet {

    private final List<TaskEntry> tasks = new LinkedList<>();
    private Supplier<Boolean> cancelIf;

    public TaskSet at(int time, Runnable task) {
        tasks.add(new TaskEntry(time, task));
        return this;
    }

    public TaskSet cancelIf(Supplier<Boolean> condition) {
        this.cancelIf = condition;
        return this;
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
            if (cancelIf != null && cancelIf.get()) return;

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
