package se.kaskware.q10.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with pride by: per on: 2016-01-30 at: 13:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //can use in method only.
public @interface TermAlias {
  public enum Priority {
    LOW, MEDIUM, HIGH
  }

  String alias();

  String lang();

  String createdBy() default "ple";

  String createdWhen() default "2015-11-03";

  String lastModified() default "2016-01-30";
}

