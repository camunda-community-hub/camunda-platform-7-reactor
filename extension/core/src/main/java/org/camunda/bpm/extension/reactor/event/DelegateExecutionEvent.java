package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.delegate.DelegateExecution;

public class DelegateExecutionEvent extends DelegateEvent<DelegateExecution> {

  private static final long serialVersionUID = 1L;

  public DelegateExecutionEvent(final DelegateExecution data) {
    super(data);
  }
}
