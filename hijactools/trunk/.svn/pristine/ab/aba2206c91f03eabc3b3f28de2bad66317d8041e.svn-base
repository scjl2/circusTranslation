package javax.safetycritical.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* Note that we do not include the meta-annotation @Inherited. */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface BoundEvent {
  String value(); /* Rename this into channel? */
  String type() default "";
}
