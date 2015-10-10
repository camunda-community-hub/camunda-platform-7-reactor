package org.camunda.bpm.extension.reactor.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import reactor.bus.EventBus;

public class PublisherTaskListener implements TaskListener {

  private final EventBus eventBus;

  public PublisherTaskListener(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void notify(final DelegateTask delegateTask) {
    eventBus.notify(CamundaReactor.topic(delegateTask), CamundaReactor.wrap(delegateTask));
  }
}
