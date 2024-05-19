package dev.tehin.tlib.utilities.task;

import dev.tehin.tlib.api.tLib;
import net.md_5.bungee.api.plugin.Plugin;
import java.util.concurrent.TimeUnit;

public class TaskUtil {

    public static void runAsync(Runnable runnable) {
        Plugin owner = tLib.get().getOwner();
        owner.getProxy().getScheduler().runAsync(owner, runnable);
    }

    public static void runSyncLater(Runnable task, int delayInTicks) {
        Plugin owner = tLib.get().getOwner();
        owner.getProxy().getScheduler().schedule(owner, task, delayInTicks * 50L, TimeUnit.MILLISECONDS);
    }

    public static void runSync(Runnable task) {
        runSyncLater(task, 1);
    }

}
