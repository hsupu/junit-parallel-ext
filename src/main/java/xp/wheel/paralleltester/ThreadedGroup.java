package xp.wheel.paralleltester;

import java.lang.annotation.*;

/**
 * @author xp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ThreadedGroup {
    Threaded[] value();
}