package org.camunda.bpm.extension.reactor.plugin.parse;

import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.form.handler.TaskFormHandler;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.delegate.TaskListener.EVENTNAME_ASSIGNMENT;
import static org.camunda.bpm.engine.delegate.TaskListener.EVENTNAME_COMPLETE;
import static org.camunda.bpm.engine.delegate.TaskListener.EVENTNAME_CREATE;
import static org.camunda.bpm.engine.delegate.TaskListener.EVENTNAME_DELETE;
import static org.mockito.Mockito.mock;

public class ReactorBpmnParseListenerTest {

  @Test
  public void add_taskListeners_to_taskDefinition() {
    TaskListener taskListener = mock(TaskListener.class);
    ExecutionListener executionListener = mock(ExecutionListener.class);
    TaskDefinition taskDefinition = new TaskDefinition(mock(TaskFormHandler.class));

    new RegisterAllBpmnParseListener(taskListener, executionListener, new ReactorProcessEnginePlugin.Configuration())
      .addTaskListener(taskDefinition);

    assertThat(taskDefinition.getTaskListeners()).hasSize(4);

    assertThat(taskDefinition.getTaskListeners(EVENTNAME_ASSIGNMENT)).containsExactly(taskListener);
    assertThat(taskDefinition.getTaskListeners(EVENTNAME_COMPLETE)).containsExactly(taskListener);
    assertThat(taskDefinition.getTaskListeners(EVENTNAME_CREATE)).containsExactly(taskListener);
    assertThat(taskDefinition.getTaskListeners(EVENTNAME_DELETE)).containsExactly(taskListener);
  }


}
