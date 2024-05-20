package dev.tehin.tlib.utilities.task;

import dev.tehin.tlib.api.tLib;
import org.bukkit.Bukkit;

import java.util.concurrent.*;

public class TaskUtil {

    // TODO: Add as TaskConfig
    private final static int CORE_POOL_SIZE = 1, MAXIMUM_POOL_SIZE = 3,  THREAD_KEEPALIVE = 60000;

    // TODO: Add dependency, compile and change to own ParallelThreads system
    private static final ThreadPoolExecutor pool =
            new ThreadPoolExecutor(CORE_POOL_SIZE,
                    MAXIMUM_POOL_SIZE,
                    THREAD_KEEPALIVE, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());

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
        pool.execute(task);
    }

    /**
     * Schedules a task for the defined future, runs inside the main thread
     *
     * @param task Task to be executed
     * @param delayInTicks Delay in Minecraft ticks (1 tick = 50ms)
     */
    public static void runSyncLater(Runnable task, int delayInTicks) {
        tLib.get().getOwner().getServer().getScheduler().runTaskLater(tLib.get().getOwner(), task, delayInTicks);
    }

    /**
     * Schedules a task for the defined future, runs outside the main thread
     *
     * @param task Task to be executed
     * @param delayInTicks Delay in Minecraft ticks (1 tick = 50ms)
     * @return Future got by our {@link ThreadPoolExecutor}
     */
    public static Future<?> runAsyncLater(Runnable task, int delayInTicks) {
        return pool.submit(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(delayInTicks * 50L);
                task.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
