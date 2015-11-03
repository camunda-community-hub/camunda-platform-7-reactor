package org.camunda.bpm.extension.example.reactor;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.CamundaSelector;
import org.camunda.bpm.extension.reactor.listener.SubscriberTaskListener;

import java.util.Arrays;
import java.util.Date;

@CamundaSelector(type = "userTask", event = TaskListener.EVENTNAME_CREATE)
public class TaskCreateListener extends SubscriberTaskListener {

  @Override
  public void notify(DelegateTask delegateTask) {
    delegateTask.addCandidateGroups(Arrays.asList(ProcessA.GROUP_1, ProcessA.GROUP_2));
    delegateTask.addCandidateGroup(ProcessA.GROUP_3);

    delegateTask.setAssignee("me");

    delegateTask.setDueDate(ProcessA.DUE_DATE);
  }
}
