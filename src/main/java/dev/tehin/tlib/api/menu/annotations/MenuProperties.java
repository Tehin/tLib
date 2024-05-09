package dev.tehin.tlib.api.menu.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MenuProperties {
    String display();
    String permission() default "";
}
