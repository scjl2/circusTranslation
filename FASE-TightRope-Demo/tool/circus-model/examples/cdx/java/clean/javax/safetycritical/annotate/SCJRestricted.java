package javax.safetycritical.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import static javax.safetycritical.annotate.Restrict.*;

@Retention(CLASS)
@Target({TYPE, FIELD, METHOD, CONSTRUCTOR})
public @interface SCJRestricted {
  public Restrict[] value() default {MAY_BLOCK, MAY_ALLOCATE, ANY_TIME};
}
