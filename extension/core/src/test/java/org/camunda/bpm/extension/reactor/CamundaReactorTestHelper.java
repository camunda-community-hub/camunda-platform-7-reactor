package org.camunda.bpm.extension.reactor;

import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.camunda.bpm.extension.reactor.event.DelegateEventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CamundaReactorTestHelper {

  private static Logger LOGGER = LoggerFactory.getLogger(CamundaReactorTestHelper.class);

  public static void listenToAllEvents(CamundaEventBus eventBus) {
    eventBus.register(SelectorBuilder.selector(), new DelegateEventConsumer() {
      @Override
      public void accept(DelegateEvent delegateEvent) {
        LOGGER.info(delegateEvent.toString());
      }
    });
  }

  public static DelegateTask delegateTask() {
    final DelegateTask task = mock(DelegateTask.class, RETURNS_DEEP_STUBS);

    when(task.getBpmnModelElementInstance().getElementType().getTypeName()).thenReturn("userTask");
    when(task.getProcessDefinitionId()).thenReturn("process:1:1");
    when(task.getEventName()).thenReturn("create");
    when(task.getTaskDefinitionKey()).thenReturn("task1");

    return task;
  }

  public static DelegateExecution delegateExecution() {
    final DelegateExecution execution = mock(DelegateExecution.class, RETURNS_DEEP_STUBS);

    when(execution.getBpmnModelElementInstance().getElementType().getTypeName()).thenReturn("serviceTask");
    when(execution.getProcessDefinitionId()).thenReturn("process:1:1");
    when(execution.getEventName()).thenReturn("start");
    when(execution.getCurrentActivityId()).thenReturn("service1");

    return execution;
  }

  public static DelegateCaseExecution delegateCaseExecution() {
    final DelegateCaseExecution execution = mock(DelegateCaseExecution.class, RETURNS_DEEP_STUBS);

    when(execution.getCmmnModelElementInstance().getElementType().getTypeName()).thenReturn("serviceTask");
    when(execution.getCaseDefinitionId()).thenReturn("process:1:1");
    when(execution.getEventName()).thenReturn("start");
    when(execution.getActivityId()).thenReturn("service1");

    return execution;
  }

}
