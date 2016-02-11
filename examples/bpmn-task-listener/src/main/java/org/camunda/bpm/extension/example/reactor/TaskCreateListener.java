package org.camunda.bpm.extension.example.reactor;

import java.util.Arrays;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;

@CamundaSelector(type = "userTask", event = TaskListener.EVENTNAME_CREATE)
public class TaskCreateListener implements TaskListener {

  public TaskCreateListener(CamundaEventBus eventBus) {
    eventBus.register(this);
  }

  @Override
  public void notify(DelegateTask delegateTask) {
    delegateTask.setDueDate(Process.DUE_DATE);

    delegateTask.addCandidateGroup(Process.GROUP_1);
    delegateTask.addCandidateGroups(Arrays.asList(Process.GROUP_2,Process.GROUP_3));
  }
}
