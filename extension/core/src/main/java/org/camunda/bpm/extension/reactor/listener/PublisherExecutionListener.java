package org.camunda.bpm.extension.reactor.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import reactor.bus.EventBus;

public class PublisherExecutionListener implements ExecutionListener {

  private final EventBus eventBus;

  public PublisherExecutionListener(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void notify(DelegateExecution delegateExecution) throws Exception {
    eventBus.notify(CamundaReactor.key(delegateExecution), CamundaReactor.wrap(delegateExecution));
  }
}
