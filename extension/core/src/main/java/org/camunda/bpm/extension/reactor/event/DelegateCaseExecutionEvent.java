package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.delegate.DelegateCaseExecution;

public class DelegateCaseExecutionEvent extends DelegateEvent<DelegateCaseExecution> {

  private static final long serialVersionUID = 1L;

  public DelegateCaseExecutionEvent(final DelegateCaseExecution data) {
    super(data);
  }
}
