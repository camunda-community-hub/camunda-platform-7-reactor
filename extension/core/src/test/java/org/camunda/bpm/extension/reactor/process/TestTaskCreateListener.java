package org.camunda.bpm.extension.reactor.process;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@CamundaSelector(type = "userTask", event = TaskListener.EVENTNAME_CREATE)
class TestTaskCreateListener implements TaskListener {

  private final Logger log = LoggerFactory.getLogger(TestTaskCreateListener.class);

  Date calledTime;

  @Override
  public void notify(final DelegateTask delegateTask) {
    calledTime = new Date();
    log.info("TestTaskCreateListener does something");
    try {
      Thread.sleep(1);
    } catch (final InterruptedException e) { }
  }
}
