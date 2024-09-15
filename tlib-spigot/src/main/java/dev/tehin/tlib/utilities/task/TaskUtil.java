package dev.tehin.tlib.utilities.task;

import dev.tehin.tlib.api.configuration.holders.TasksConfig;
import dev.tehin.tlib.api.tLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.*;

public class TaskUtil {

    /**
     * Runs the task inside the main thread.
     * <br/><br/>
     * If we are already inside it, run the task instantly.
     * <br/>
     * If not, schedule the task for the next bukkit tick
     *
     * @param task The task to be done in the main thread
     */
    public static void runSync(Runnable task) {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(tLib.get().getOwner(), task);
            return;
        }

        task.run();
    }

    /**
     * Instantly run the task outside the main thread
     *
     * @param task The task to be executed
     */
    public static void runAsync(Runnable task) {
        Plugin owner = tLib.get().getOwner();

        Bukkit.getScheduler().runTaskAsynchronously(owner, task);
    }

    /**
     * Schedules a task for the defined future, runs inside the main thread
     *
     * @param task Task to be executed
     * @param delayInTicks Delay in Minecraft ticks (1 tick = 50ms)
     */
    public static void runSyncLater(Runnable task, int delayInTicks) {
        Plugin owner = tLib.get().getOwner();

        owner.getServer().getScheduler().runTaskLater(owner, task, delayInTicks);
    }

    /**
     * Schedules a task for the defined future, runs outside the main thread <br>
     * Be aware that this may freeze the async pool, use carefully
     *
     * @param task Task to be executed
     * @param delayInTicks Delay in Minecraft ticks (1 tick = 50ms)
     */
    public static void runAsyncLater(Runnable task, int delayInTicks) {
        Plugin owner = tLib.get().getOwner();

        Bukkit.getScheduler().runTaskLaterAsynchronously(owner, task, delayInTicks);
    }

    public static TaskSet createSet() {
        return new TaskSet();
    }
}
