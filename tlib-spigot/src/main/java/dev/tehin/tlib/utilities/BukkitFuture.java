package dev.tehin.tlib.utilities;

import dev.tehin.tlib.utilities.task.TaskUtil;
import lombok.*;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

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
    private final boolean actionAsync;

    // Whether the consumer is expected to run async
    private boolean consumerAsync;

    private BukkitFuture(boolean async) {
        this.actionAsync = async;
        this.consumerAsync = async;
    }

    public static <T> BukkitFuture<T> createAsync() {
        return new BukkitFuture<>(true);
    }

    public static <T> BukkitFuture<T> createSync() {
        return new BukkitFuture<>(false);
    }

    public void complete(T object) {
        try {
            if (consumer == null) {
                this.pending = new PendingResult<>(object, null);
                return;
            }

            TaskUtil.run(consumerAsync, () -> consumer.accept(object, null));
        } catch (Exception e) {
            if (consumer == null) {
                this.pending = new PendingResult<>(null, e);
                return;
            }

            TaskUtil.run(consumerAsync, (() -> consumer.accept(null, e)));
        }
    }

    public static <T> BukkitFuture<T> supplyAsync(Supplier<T> task) {
        BukkitFuture<T> future = createAsync();

        Runnable result = () -> future.complete(task.get());
        TaskUtil.runAsync(result);

        return future;
    }

    public static <T> BukkitFuture<T> supplySync(Supplier<T> task) {
        BukkitFuture<T> future = createSync();

        Runnable result = () -> future.complete(task.get());
        TaskUtil.runSync(result);

        return future;
    }

    public BukkitFuture<T> whenCompleteSync(BiConsumer<T, Throwable> consumer) {
        return whenComplete(false, consumer);
    }

    public BukkitFuture<T> whenCompleteAsync(BiConsumer<T, Throwable> consumer) {
        return whenComplete(true, consumer);
    }

    public BukkitFuture<T> whenComplete(BiConsumer<T, Throwable> consumer) {
        return whenComplete(this.actionAsync, consumer);
    }

    public BukkitFuture<T> whenComplete(boolean async, BiConsumer<T, Throwable> consumer) {
        this.consumerAsync = async;
        this.consumer = consumer;

        // A pending result is waiting, execute it now
        if (pending != null) {
            Runnable task = () -> consumer.accept(pending.getResult(), pending.getThrowable());
            TaskUtil.run(async, task);
        }

        return this;
    }

}
