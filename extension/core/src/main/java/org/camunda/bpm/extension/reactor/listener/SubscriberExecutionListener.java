package org.camunda.bpm.extension.reactor.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.event.DelegateExecutionEvent;
import reactor.bus.Event;
import reactor.fn.Consumer;

public abstract class SubscriberExecutionListener implements ExecutionListener, Consumer<DelegateExecutionEvent> {

  public static SubscriberExecutionListener create(final ExecutionListener executionListener) {
    return new SubscriberExecutionListener() {
      @Override
      public void notify(DelegateExecution delegateExecution) throws Exception {
        executionListener.notify(delegateExecution);
      }
    };
  }

  @Override
  public void accept(final DelegateExecutionEvent delegateExecutionEvent) {
    try {
      notify(delegateExecutionEvent.getData());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
