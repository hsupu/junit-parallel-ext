package cc.xpcas.junit.ext.parallel;

import java.lang.annotation.*;

/**
 * @author xp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeforeCase {
    String value();
}