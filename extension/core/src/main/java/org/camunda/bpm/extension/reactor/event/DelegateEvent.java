package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.delegate.BaseDelegateExecution;
import org.camunda.bpm.engine.delegate.BpmnModelExecutionContext;
import reactor.bus.Event;

public abstract class DelegateEvent<T extends BpmnModelExecutionContext> extends Event<T> {

  public DelegateEvent(T data) {
    super(data);
    getId();
  }
}
