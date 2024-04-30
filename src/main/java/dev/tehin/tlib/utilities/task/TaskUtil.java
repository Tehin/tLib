package dev.tehin.tlib.utilities.task;

import dev.tehin.tlib.api.tLib;
import org.bukkit.Bukkit;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class TaskUtil {

    public static void runAsync(Runnable runnable) {
        ForkJoinPool.commonPool().execute(runnable);
    }

    public static void runSyncLater(Runnable task, int delayInTicks) {
        tLib.get().getOwner().getServer().getScheduler().runTaskLater(tLib.get().getOwner(), task, delayInTicks);
    }

    public static void runLaterAsync(Runnable task, int delayInTicks) {
        ForkJoinPool.commonPool().submit(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(delayInTicks * 50L);
                task.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void runSync(Runnable task) {
        Bukkit.getScheduler().runTask(tLib.get().getOwner(), task);
    }

    /**
     * This method should be called to not always use the bukkit scheduler.
     * If the call is on the main thread, the task will run normally,
     * if not, it will be given to the main thread.
     * (Useful for dispatch commands or entity adds)
     *
     * @param task The task that has to be done in the main thread
     */
    public static void ensureMain(Runnable task) {
        if (!Bukkit.isPrimaryThread()) {
            runSync(task);
            return;
        }

        task.run();
    }

    public static void nextTick(Runnable runnable) {
        runSyncLater(runnable, 1);
    }

}
