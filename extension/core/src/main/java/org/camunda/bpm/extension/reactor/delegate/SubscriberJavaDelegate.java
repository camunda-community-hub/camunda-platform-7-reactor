package org.camunda.bpm.extension.reactor.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import reactor.bus.EventBus;

public class SubscriberJavaDelegate implements JavaDelegate{

  private final EventBus eventBus;

  public SubscriberJavaDelegate(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void execute(final DelegateExecution execution) throws Exception {
    eventBus.notify(CamundaReactor.selector(execution), CamundaReactor.wrap(execution));
  }
}
