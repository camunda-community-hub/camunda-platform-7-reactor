package org.camunda.bpm.extension.example.reactor;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CamundaSelector(type = "userTask", event = TaskListener.EVENTNAME_ASSIGNMENT)
public class TaskAssignListener implements TaskListener {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  public TaskAssignListener() {
    CamundaReactor.eventBus().register(this);
  }

  @Override
  public void notify(DelegateTask delegateTask) {

    logger.debug("assigned to {}", delegateTask.getAssignee());

  }

}
