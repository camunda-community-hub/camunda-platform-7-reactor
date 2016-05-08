package org.camunda.bpm.extension.example.reactor;


import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;

@CamundaSelector(type = "humanTask", event = TaskListener.EVENTNAME_CREATE)
public class CaseTaskCreateListener implements TaskListener {

  public CaseTaskCreateListener(final CamundaEventBus eventBus) {
    eventBus.register(this);
  }

  @Override
  public void notify(DelegateTask delegateTask) {
    delegateTask.setAssignee("me");
  }
}
