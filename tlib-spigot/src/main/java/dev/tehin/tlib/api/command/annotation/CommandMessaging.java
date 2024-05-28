package dev.tehin.tlib.api.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMessaging {
    String usage();
    String noPermission();
}
