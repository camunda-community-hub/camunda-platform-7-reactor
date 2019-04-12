package org.camunda.bpm.extension.reactor.process;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;

import java.util.Date;

@CamundaSelector(type = "userTask", event = TaskListener.EVENTNAME_CREATE)
class TestTaskCreateListener implements TaskListener {
  Date calledTime;

  @Override
  public void notify(final DelegateTask delegateTask) {
    calledTime = new Date();
    try {
      Thread.sleep(1);
    } catch (final InterruptedException e) { }
  }
}
