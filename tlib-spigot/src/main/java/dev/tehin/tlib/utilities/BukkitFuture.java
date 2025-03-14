package dev.tehin.tlib.utilities;

import dev.tehin.tlib.utilities.task.TaskUtil;
import lombok.*;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class BukkitFuture<T> {

    // A result was given but the completion callback has not been assigned
    @AllArgsConstructor
    @Getter
    private static class PendingResult<T> {
        T result;
        Throwable throwable;
    }

    private BiConsumer<T, Throwable> consumer;
    private PendingResult<T> pending;

    // Whether the task is expected to run async
    private boolean async;

    public void complete(T object) {
        try {
            if (consumer != null) {
                consumer.accept(object, null);
            } else {
                this.pending = new PendingResult<>(object, null);
            }
        } catch (Exception e) {
            if (consumer != null) {
                consumer.accept(null, e);
            } else {
                this.pending = new PendingResult<>(null, e);
            }
        }
    }

    public static <T> BukkitFuture<T> supplyAsync(Supplier<T> task) {
        BukkitFuture<T> future = new BukkitFuture<>();
        future.async = true;

        Runnable result = () -> future.complete(task.get());
        TaskUtil.runAsync(result);

        return future;
    }

    public static <T> BukkitFuture<T> supplySync(Supplier<T> task) {
        BukkitFuture<T> future = new BukkitFuture<>();
        future.async = false;

        Runnable result = () -> future.complete(task.get());
        TaskUtil.runSync(result);

        return future;
    }

    public BukkitFuture<T> whenComplete(BiConsumer<T, Throwable> consumer) {
        this.consumer = consumer;

        // A pending result is waiting, execute it now
        if (pending != null) {
            Runnable task = () -> consumer.accept(pending.getResult(), pending.getThrowable());

            if (async) {
                TaskUtil.runAsync(task);
            } else {
                TaskUtil.runSync(task);
            }
        }

        return this;
    }

}
