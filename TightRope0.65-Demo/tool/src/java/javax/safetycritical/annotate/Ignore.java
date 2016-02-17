package javax.safetycritical.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({
  ElementType.CONSTRUCTOR,
  ElementType.FIELD,
  ElementType.LOCAL_VARIABLE,
  ElementType.METHOD,
  ElementType.PARAMETER,
  ElementType.TYPE})
@Inherited
public @interface Ignore { }
