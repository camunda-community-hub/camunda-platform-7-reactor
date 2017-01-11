package org.camunda.bpm.extension.reactor.spring.listener;

import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class ReactorExecutionListener implements ExecutionListener {

  @Autowired
  public void register(final CamundaEventBus camundaEventBus) {
    camundaEventBus.register(this);
  }

}
