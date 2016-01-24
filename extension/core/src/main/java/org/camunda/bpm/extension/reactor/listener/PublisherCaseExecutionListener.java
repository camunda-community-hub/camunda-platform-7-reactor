package org.camunda.bpm.extension.reactor.listener;

import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import reactor.bus.EventBus;

public class PublisherCaseExecutionListener implements CaseExecutionListener {

  private final EventBus eventBus;

  public PublisherCaseExecutionListener(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void notify(DelegateCaseExecution caseExecution) throws Exception {
    eventBus.notify(CamundaReactor.key(caseExecution), CamundaReactor.wrap(caseExecution));
  }
}
