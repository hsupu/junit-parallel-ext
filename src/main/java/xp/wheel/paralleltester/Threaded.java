package xp.wheel.paralleltester;

import java.lang.annotation.*;

/**
 * @author xp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ThreadedGroup.class)
public @interface Threaded {
    int number() default 1;
    String tag() default "";
}