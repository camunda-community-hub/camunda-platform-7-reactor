package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import reactor.bus.Event;

public class DelegateExecutionEvent extends DelegateEvent<DelegateExecution> {

  public DelegateExecutionEvent(final DelegateExecution data) {
    super(data);
  }
}
