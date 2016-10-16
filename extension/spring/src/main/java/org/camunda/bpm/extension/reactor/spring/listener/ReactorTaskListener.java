package org.camunda.bpm.extension.reactor.spring.listener;

import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class ReactorTaskListener implements TaskListener {

  @Autowired
  public void register(CamundaEventBus camundaEventBus) {
    camundaEventBus.register(this);
  }

}
