package org.camunda.bpm.extension.reactor.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import reactor.bus.EventBus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.delegate.TaskListener.EVENTNAME_CREATE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ReactorTaskListenerTest {

  private static final String ID = UUID.randomUUID().toString();

  private final DelegateTask delegateTask = mock(DelegateTask.class);

  private final EventBus eventBus = mock(EventBus.class);

  private final ReactorTaskListener listener = new ReactorTaskListener(eventBus);

  private ArgumentCaptor<String> topicCaptor  = ArgumentCaptor.forClass(String.class);
  private ArgumentCaptor<DelegateTaskEvent> eventCaptor  = ArgumentCaptor.forClass(DelegateTaskEvent.class);

  @Test
  public void notify_eventBus() {
    when(delegateTask.getProcessDefinitionId()).thenReturn("process");
    when(delegateTask.getTaskDefinitionKey()).thenReturn("task");
    when(delegateTask.getEventName()).thenReturn(EVENTNAME_CREATE);
    when(delegateTask.getExecutionId()).thenReturn(ID);

    listener.notify(delegateTask);

    verify(eventBus).notify(topicCaptor.capture(), eventCaptor.capture());

    assertThat(topicCaptor.getValue()).isEqualTo(CamundaReactor.topic(delegateTask));
    assertThat(eventCaptor.getValue().getData().getExecutionId()).isEqualTo(ID);
  }
}
