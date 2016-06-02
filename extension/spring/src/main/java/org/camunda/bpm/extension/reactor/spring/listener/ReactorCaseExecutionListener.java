package org.camunda.bpm.extension.reactor.spring.listener;

import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class ReactorCaseExecutionListener implements CaseExecutionListener {

  @Autowired
  public void register(CamundaEventBus camundaEventBus) {
    camundaEventBus.register(this);
  }

}
