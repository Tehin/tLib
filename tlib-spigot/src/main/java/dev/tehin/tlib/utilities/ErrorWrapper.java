package dev.tehin.tlib.utilities;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ErrorWrapper {

    public static <T> T wrap(Supplier<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return defaultValue;
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
}