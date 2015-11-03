package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.delegate.BpmnModelExecutionContext;

import reactor.bus.Event;

public abstract class DelegateEvent<T extends BpmnModelExecutionContext> extends Event<T> {

  private static final long serialVersionUID = 1L;

  public DelegateEvent(T data) {
    super(data);
    getId();
  }
}
