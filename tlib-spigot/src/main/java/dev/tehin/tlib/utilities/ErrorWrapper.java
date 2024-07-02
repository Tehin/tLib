package dev.tehin.tlib.utilities;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ErrorWrapper {

    public static <T> T wrap(Supplier<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static <T> T wrapWithSupplier(Supplier<T> supplier, Supplier<T> defaultValue) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return defaultValue.get();
        }
    }

    public static <T> T wrap(CompletableFuture<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static <T> T wrapNullable(Supplier<T> supplier, T defaultValue) {
        T result = supplier.get();

        if (result == null) return defaultValue;
        return result;
    }

    public static void callOnException(Runnable runnable, Consumer<Exception> callback) {
        try {
            runnable.run();
        } catch (Exception e) {
            if (callback == null) return;
            callback.accept(e);
        }
    }
}