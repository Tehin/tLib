package dev.tehin.tlib.utilities;

import dev.tehin.tlib.utilities.task.TaskUtil;
import lombok.RequiredArgsConstructor;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class BukkitFuture<T> {

    private BiConsumer<T, Throwable> consumer;

    public void complete(T object) {
        try {
            if (consumer != null) consumer.accept(object, null);
        } catch (Exception e) {
            e.printStackTrace();
            consumer.accept(null, e);
        }
    }

    public static <T> BukkitFuture<T> supplyAsync(Supplier<T> task) {
        BukkitFuture<T> future = new BukkitFuture<>();

        Runnable result = () -> future.complete(task.get());
        TaskUtil.runAsync(result);

        return future;
    }

    public static <T> BukkitFuture<T> supplySync(Supplier<T> task) {
        BukkitFuture<T> future = new BukkitFuture<>();

        Runnable result = () -> future.complete(task.get());
        TaskUtil.runSync(result);

        return future;
    }

    public BukkitFuture<T> whenComplete(BiConsumer<T, Throwable> consumer) {
        this.consumer = consumer;
        return this;
    }

}
