package javax.safetycritical.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* It is still an open issue what should go into this annotation. */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
@Inherited
public @interface ActiveClass { }
