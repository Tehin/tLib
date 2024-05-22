package dev.tehin.tlib.utilities.task;

import dev.tehin.tlib.api.configuration.holders.TasksConfig;
import dev.tehin.tlib.api.tLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.*;

public class TaskUtil {


    // TODO: Add dependency, compile and change to own ParallelThreads system
    private static ThreadPoolExecutor POOL = null;

    private static void loadPool(tLib lib) {
        TasksConfig config = lib.getConfig().tasks();

        POOL = new ThreadPoolExecutor(config.getCorePoolSize(),
                config.getMaximumPoolSize(),
                config.getThreadKeepAliveInMs(), TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
    }

    /**
     * Runs the task inside the main thread.
     * <br/><br/>
     * If we are already inside it, run the task instantly.
     * <br/>
     * If not, schedule the task for the next bukkit tick
     *
     * @param task The task to be done in the main thread
     */
    public static void runSync(Runnable task, Plugin plugin) {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(plugin, task);
            return;
        }

        task.run();
    }


    /**
     * Instantly run the task outside the main thread
     *
     * @param task The task to be executed
     */
    public static void runAsync(Runnable task, tLib lib) {
        if (POOL == null) loadPool(lib);

        POOL.execute(task);
    }

    /**
     * Schedules a task for the defined future, runs inside the main thread
     *
     * @param task Task to be executed
     * @param delayInTicks Delay in Minecraft ticks (1 tick = 50ms)
     */
    public static void runSyncLater(Runnable task, int delayInTicks, Plugin plugin) {
        plugin.getServer().getScheduler().runTaskLater(plugin, task, delayInTicks);
    }

    /**
     * Schedules a task for the defined future, runs outside the main thread
     *
     * @param task Task to be executed
     * @param delayInTicks Delay in Minecraft ticks (1 tick = 50ms)
     * @return Future got by our {@link ThreadPoolExecutor}
     */
    public static Future<?> runAsyncLater(Runnable task, int delayInTicks, tLib lib) {
        if (POOL == null) loadPool(lib);

        return POOL.submit(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(delayInTicks * 50L);
                task.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
