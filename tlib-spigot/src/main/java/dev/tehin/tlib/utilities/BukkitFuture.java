package dev.tehin.tlib.utilities;

import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.utilities.task.TaskUtil;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class BukkitFuture<T> {

    public enum LibThread {
        BUKKIT,
        PARALLEL
    }

    private final tLib lib;

    private Consumer<T> sync, async;
    private Consumer<Exception> error;

    public void complete(T object) {
        if (sync != null) {
            TaskUtil.runSync(() -> {
                ErrorWrapper.callOnException(() -> sync.accept(object), error);
            }, lib.getOwner());
        }

        if (async != null) {
            TaskUtil.runAsync(() -> {
                ErrorWrapper.callOnException(() -> async.accept(object), error);
            }, lib);
        }
    }

    public static <T> BukkitFuture<T> consume(tLib lib, LibThread thread, Supplier<T> task) {
        BukkitFuture<T> future = new BukkitFuture<>(lib);

        Runnable result = () -> future.complete(task.get());

        switch (thread) {
            case BUKKIT: {
                TaskUtil.runSync(result, lib.getOwner());
                break;
            }
            case PARALLEL: {
                TaskUtil.runAsync(result, lib);
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
            case PARALLEL:
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
