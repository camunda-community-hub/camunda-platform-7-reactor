package org.camunda.bpm.extension.example.reactor;


import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;

@CamundaSelector(type = "humanTask")
public class CaseHumanTaskLifecycleListener implements CaseExecutionListener {

  public CaseHumanTaskLifecycleListener(CamundaEventBus eventBus) {
    eventBus.register(this);
  }

  @Override
  public void notify(DelegateCaseExecution delegateCaseExecution) throws Exception {
    System.out.printf("%s %s", delegateCaseExecution.getEventName(), delegateCaseExecution.getActivityName());
  }
}
