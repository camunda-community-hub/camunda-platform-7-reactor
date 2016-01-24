package org.camunda.bpm.extension.reactor.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.event.DelegateCaseExecutionEvent;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.bus.EventBus;


public class PublisherCaseExecutionListenerTest {

  private static final String ID = UUID.randomUUID().toString();

  private final DelegateCaseExecution delegateCaseExecution = mock(DelegateCaseExecution.class, Mockito.RETURNS_DEEP_STUBS);

  private final EventBus eventBus = mock(EventBus.class);

  private final PublisherCaseExecutionListener listener = new PublisherCaseExecutionListener(eventBus);

  private ArgumentCaptor<String> topicCaptor  = ArgumentCaptor.forClass(String.class);
  private ArgumentCaptor<DelegateCaseExecutionEvent> eventCaptor  = ArgumentCaptor.forClass(DelegateCaseExecutionEvent.class);

  @Test
  public void notify_eventBus() throws Exception {
    when(delegateCaseExecution.getCaseDefinitionId()).thenReturn("process");
    when(delegateCaseExecution.getActivityId()).thenReturn("activity");
    when(delegateCaseExecution.getEventName()).thenReturn(ExecutionListener.EVENTNAME_TAKE);
    when(delegateCaseExecution.getId()).thenReturn(ID);
    when(delegateCaseExecution.getCmmnModelElementInstance().getElementType().getTypeName()).thenReturn("type");

    listener.notify(delegateCaseExecution);

    verify(eventBus).notify(topicCaptor.capture(), eventCaptor.capture());

    assertThat(topicCaptor.getValue()).isEqualTo(CamundaReactor.key(delegateCaseExecution));
    assertThat(eventCaptor.getValue().getData().getId()).isEqualTo(ID);
  }
}
