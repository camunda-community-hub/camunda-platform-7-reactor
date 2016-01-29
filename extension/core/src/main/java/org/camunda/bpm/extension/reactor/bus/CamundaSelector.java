package org.camunda.bpm.extension.reactor.bus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CamundaSelector {

  public String type() default "";
  public String element() default "";
  public String process() default "";
  public String event() default "";

}
