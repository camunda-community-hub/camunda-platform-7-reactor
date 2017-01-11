package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import reactor.bus.Event;

public abstract class DelegateEvent<T> extends Event<T> {

  public static DelegateTaskEvent wrap(final DelegateTask delegateTask) {
    return new DelegateTaskEvent(delegateTask);
  }

  public static DelegateExecutionEvent wrap(final DelegateExecution delegateExecution) {
    return new DelegateExecutionEvent(delegateExecution);
  }

  public static DelegateCaseExecutionEvent wrap(final DelegateCaseExecution delegateCaseExecution) {
    return new DelegateCaseExecutionEvent(delegateCaseExecution);
  }

  private static final long serialVersionUID = 1L;

  public DelegateEvent(T data) {
    super(data);
    getId();
  }
}
