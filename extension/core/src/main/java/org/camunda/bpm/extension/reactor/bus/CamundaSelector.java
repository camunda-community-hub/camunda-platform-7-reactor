package org.camunda.bpm.extension.reactor.bus;


import org.camunda.bpm.extension.reactor.bus.SelectorBuilder.Context;
import org.camunda.bpm.extension.reactor.event.ProcessEnginePluginEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.camunda.bpm.extension.reactor.bus.SelectorBuilder.EMPTY;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CamundaSelector {

  String type() default EMPTY;
  String element() default EMPTY;
  String process() default EMPTY;
  String event() default EMPTY;
  Context context() default Context.bpmn;

}
