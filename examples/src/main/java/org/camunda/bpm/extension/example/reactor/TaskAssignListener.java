package org.camunda.bpm.extension.example.reactor;

import static org.camunda.bpm.extension.reactor.CamundaSelector.Queue.tasks;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.CamundaSelector;
import org.camunda.bpm.extension.reactor.listener.SubscriberTaskListener;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;

@CamundaSelector(queue = tasks, type = "userTask", event = TaskListener.EVENTNAME_ASSIGNMENT)
public class TaskAssignListener extends SubscriberTaskListener {

  public TaskAssignListener() {
    register(ReactorProcessEnginePlugin.CAMUNDA_EVENTBUS);
  }

  @Override
  public void notify(DelegateTask delegateTask) {

    System.out.println("assigned to " + delegateTask.getAssignee());

  }

}
