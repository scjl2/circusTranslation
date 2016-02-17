package javax.safetycritical.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* This annotation is not used in the paper. It is just to get around the
 * problem of performing control low analysis of the code. */

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
@Inherited
public @interface FrameworkMethod { }
