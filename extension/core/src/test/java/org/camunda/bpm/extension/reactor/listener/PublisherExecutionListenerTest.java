package org.camunda.bpm.extension.reactor.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.event.DelegateExecutionEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import reactor.bus.EventBus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class PublisherExecutionListenerTest {

  private static final String ID = UUID.randomUUID().toString();

  private final DelegateExecution delegateExecution = mock(DelegateExecution.class);

  private final EventBus eventBus = mock(EventBus.class);

  private final PublisherExecutionListener listener = new PublisherExecutionListener(eventBus);

  private ArgumentCaptor<String> topicCaptor  = ArgumentCaptor.forClass(String.class);
  private ArgumentCaptor<DelegateExecutionEvent> eventCaptor  = ArgumentCaptor.forClass(DelegateExecutionEvent.class);

  @Test
  public void notify_eventBus() throws Exception {
    when(delegateExecution.getProcessDefinitionId()).thenReturn("process");
    when(delegateExecution.getCurrentActivityId()).thenReturn("activity");
    when(delegateExecution.getEventName()).thenReturn(ExecutionListener.EVENTNAME_TAKE);
    when(delegateExecution.getId()).thenReturn(ID);

    listener.notify(delegateExecution);

    verify(eventBus).notify(topicCaptor.capture(), eventCaptor.capture());

    assertThat(topicCaptor.getValue()).isEqualTo(CamundaReactor.topic(delegateExecution));
    assertThat(eventCaptor.getValue().getData().getId()).isEqualTo(ID);
  }
}
