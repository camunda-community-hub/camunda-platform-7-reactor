package org.camunda.bpm.extension.reactor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CamundaSelector {

  Queue queue() default Queue.none;
  String type() default "";
  String element() default "";
  String process() default "";
  String event() default "";

  enum Queue {
    tasks,
    processExecutions,
    caseExecutions,
    none
  }

}
