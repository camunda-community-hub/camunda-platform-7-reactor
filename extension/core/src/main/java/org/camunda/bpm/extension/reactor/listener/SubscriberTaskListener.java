package org.camunda.bpm.extension.reactor.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import reactor.bus.Event;
import reactor.fn.Consumer;

public abstract class SubscriberTaskListener extends SubscriberListener<DelegateTaskEvent> implements TaskListener {

  public static SubscriberTaskListener create(final TaskListener taskListener) {
    return new SubscriberTaskListener() {
      @Override
      public void notify(DelegateTask delegateTask) {
        taskListener.notify(delegateTask);
      }
    };
  }

  @Override
  public void accept(final DelegateTaskEvent delegateTaskEvent) {
    logger.debug("Received: {}", delegateTaskEvent);
    notify(delegateTaskEvent.getData());
  }
}
