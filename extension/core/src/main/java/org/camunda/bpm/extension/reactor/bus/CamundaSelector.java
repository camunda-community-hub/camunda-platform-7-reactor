package org.camunda.bpm.extension.reactor.bus;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.camunda.bpm.extension.reactor.bus.SelectorBuilder.Context;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CamundaSelector {

  /**
   * Restrict listening to a certain activity type. You can use the type constants provided by {@link org.camunda.bpm.engine.ActivityTypes}.
   *
   * @return am activity type or "" (meaning: all activity types).
   */
  String type() default "";

  /**
   * Restrict listening to a certain activityId (for example the taskDefinitionKey).
   *
   * @return an activityId of a process element or default "" (meaning: all possible activities)
   */
  String element() default "";

  /**
   * Restrict listening to a certain processDefinitionKey.
   *
   * @return one of the deployed processes definition key, or default "" (meaning: all processes)
   */
  String process() default "";

  /**
   * Restrict listening to the events specified in {@link org.camunda.bpm.engine.delegate.TaskListener} and {@link org.camunda.bpm.engine.delegate.ExecutionListener}.
   * @return camunda event to listen to (for example {@link org.camunda.bpm.engine.delegate.TaskListener#EVENTNAME_CREATE}, or default "" (meaning: all events).
   */
  String event() default "";

  /**
   * The Context can be either {@link Context#bpmn} (for processes), {@link Context#cmmn} (for cases) or {@link Context#task} (for user tasks, no matter if case or process).
   * @return one of the context values, default {@link Context#bpmn}
   */
  Context context() default Context.bpmn;

}
