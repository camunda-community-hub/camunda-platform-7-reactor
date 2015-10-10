package org.camunda.bpm.extension.reactor.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import reactor.bus.Event;
import reactor.fn.Consumer;

public abstract class SubscriberTaskListener implements TaskListener, Consumer<Event<DelegateTask>> {

  public static SubscriberTaskListener create(final TaskListener taskListener) {
    return new SubscriberTaskListener() {
      @Override
      public void notify(DelegateTask delegateTask) {
        taskListener.notify(delegateTask);
      }
    };
  }

  @Override
  public void accept(final Event<DelegateTask> delegateTaskEvent) {
    notify(delegateTaskEvent.getData());
  }
}
