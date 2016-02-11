package org.camunda.bpm.extension.example.reactor;


import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CamundaSelector(type = "humanTask")
public class CaseHumanTaskLifecycleListener implements CaseExecutionListener {
  
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public CaseHumanTaskLifecycleListener(CamundaEventBus eventBus) {
    eventBus.register(this);
  }

  @Override
  public void notify(DelegateCaseExecution delegateCaseExecution) throws Exception {
    logger.info("{} {}", delegateCaseExecution.getEventName(), delegateCaseExecution.getActivityName());
  }
}
