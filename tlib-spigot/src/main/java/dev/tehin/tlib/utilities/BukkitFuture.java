package dev.tehin.tlib.utilities;

import dev.tehin.tlib.utilities.task.TaskUtil;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class BukkitFuture<T> {

    public enum LibThread {
        BUKKIT,
        ASYNC
    }

    private Consumer<T> sync, async;
    private Consumer<Exception> error;

    public void complete(T object) {
        if (sync != null) {
            TaskUtil.runSync(() -> ErrorWrapper.callOnException(() -> sync.accept(object), error));
        }

        if (async != null) {
            TaskUtil.runAsync(() -> ErrorWrapper.callOnException(() -> async.accept(object), error));
        }
    }

    public static <T> BukkitFuture<T> consume(LibThread thread, Supplier<T> task) {
        BukkitFuture<T> future = new BukkitFuture<>();

        Runnable result = () -> future.complete(task.get());

        switch (thread) {
            case BUKKIT: {
                TaskUtil.runSync(result);
                break;
            }
            case ASYNC: {
                TaskUtil.runAsync(result);
                break;
            }
        }

        return future;
    }

    public BukkitFuture<T> then(LibThread thread, Consumer<T> consumer) {
        switch (thread) {
            case BUKKIT:
                this.sync = consumer;
                break;
            case ASYNC:
                this.async = consumer;
                break;
        }

        return this;
    }

    public BukkitFuture<T> error(Consumer<Exception> handler) {
        this.error = handler;
        return this;
    }

}
