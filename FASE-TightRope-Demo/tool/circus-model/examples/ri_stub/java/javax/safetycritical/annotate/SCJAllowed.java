package javax.safetycritical.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.*;
import static java.lang.annotation.ElementType.*;

@Retention(CLASS)
@Target({TYPE, FIELD, METHOD, CONSTRUCTOR})
public @interface SCJAllowed {
   public Level value() default Level.LEVEL_0;
}
