package org.camunda.bpm.extension.reactor.event;

import reactor.bus.Event;

public abstract class DelegateEvent<T> extends Event<T> {

  private static final long serialVersionUID = 1L;

  public DelegateEvent(T data) {
    super(data);
    getId();
  }
}
