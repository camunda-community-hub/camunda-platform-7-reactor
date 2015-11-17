package org.camunda.bpm.extension.reactor.plugin;

import org.camunda.bpm.engine.impl.form.handler.TaskFormHandler;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.extension.reactor.listener.PublisherExecutionListener;
import org.camunda.bpm.extension.reactor.listener.PublisherTaskListener;
import org.camunda.bpm.extension.reactor.plugin.ReactorBpmnParseListener;
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
    PublisherTaskListener taskListener = mock(PublisherTaskListener.class);
    PublisherExecutionListener executionListener = mock(PublisherExecutionListener.class);
    TaskDefinition taskDefinition = new TaskDefinition(mock(TaskFormHandler.class));

    new ReactorBpmnParseListener(taskListener, executionListener).addTaskListener(taskDefinition);

    assertThat(taskDefinition.getTaskListeners()).hasSize(4);

    assertThat(taskDefinition.getTaskListener(EVENTNAME_ASSIGNMENT)).containsExactly(taskListener);
    assertThat(taskDefinition.getTaskListener(EVENTNAME_COMPLETE)).containsExactly(taskListener);
    assertThat(taskDefinition.getTaskListener(EVENTNAME_CREATE)).containsExactly(taskListener);
    assertThat(taskDefinition.getTaskListener(EVENTNAME_DELETE)).containsExactly(taskListener);
  }


}
