package org.camunda.bpm.extension.example.reactor;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;

@CamundaSelector(type = "userTask", event = TaskListener.EVENTNAME_ASSIGNMENT)
public class TaskAssignListener implements TaskListener {

  public TaskAssignListener() {
    CamundaReactor.eventBus().register(this);
  }

  @Override
  public void notify(DelegateTask delegateTask) {

    System.out.println("assigned to " + delegateTask.getAssignee());

  }

}
