package dev.tehin.tlib.api.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandArgsStructure {

    String usage();
    int fixedLength() default -1;
    Class<?>[] structure() default {};
}
