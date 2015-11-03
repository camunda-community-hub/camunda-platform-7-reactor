package org.camunda.bpm.extension.reactor.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.extension.reactor.event.DelegateExecutionEvent;

public abstract class SubscriberExecutionListener extends SubscriberListener<DelegateExecutionEvent> implements ExecutionListener {

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
      logger.debug("Received: {}", delegateExecutionEvent);
      notify(delegateExecutionEvent.getData());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
