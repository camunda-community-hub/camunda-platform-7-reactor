package org.camunda.bpm.extension.reactor.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.SelectorBuilder;
import org.camunda.bpm.extension.reactor.event.DelegateExecutionEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.Selector;
import reactor.fn.Consumer;

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
