package org.camunda.bpm.extension.reactor.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import reactor.bus.EventBus;

public class ReactorExecutionListener implements ExecutionListener {

  private final EventBus eventBus;

  public ReactorExecutionListener(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void notify(DelegateExecution delegateExecution) throws Exception {
    eventBus.notify(CamundaReactor.topic(delegateExecution), CamundaReactor.wrap(delegateExecution));
  }
}
