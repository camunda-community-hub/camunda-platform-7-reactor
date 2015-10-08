package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import reactor.bus.Event;

public class DelegateExecutionEvent extends Event<DelegateExecution> {

  public DelegateExecutionEvent(DelegateExecution data) {
    super(data);
    getId();
  }
}
