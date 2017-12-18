package org.camunda.bpm.extension.reactor;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.repository.CaseDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.camunda.bpm.extension.reactor.event.DelegateEventConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamundaReactorTestHelper {

  public static final String PROCESS_KEY = "process";
  public static final String CASE_KEY = "process";
  public static final String REVISION = "1";
  public static final String DEPLOYMENT_ID = "1";
  public static final String PROCESS_DEFINITION_ID = String.join(":", PROCESS_KEY, REVISION, DEPLOYMENT_ID);
  public static final String CASE_DEFINITION_ID = String.join(":", CASE_KEY, REVISION, DEPLOYMENT_ID);

  private static Logger LOGGER = LoggerFactory.getLogger(CamundaReactorTestHelper.class);

  public static ProcessDefinition processDefinition() {
    ProcessDefinition processDefinition = mock(ProcessDefinition.class);
    when(processDefinition.getId()).thenReturn(PROCESS_DEFINITION_ID);
    when(processDefinition.getKey()).thenReturn(PROCESS_KEY);
    return processDefinition;
  }

  public static CaseDefinition caseDefinition() {
    CaseDefinition caseDefinition = mock(CaseDefinition.class);
    when(caseDefinition.getId()).thenReturn(CASE_DEFINITION_ID);
    when(caseDefinition.getKey()).thenReturn(CASE_KEY);
    return caseDefinition;
  }

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

    when(task.getTaskDefinitionKey()).thenReturn("task1");
    when(task.getProcessDefinitionId()).thenReturn(PROCESS_DEFINITION_ID);
    when(task.getEventName()).thenReturn("create");

    return task;
  }

  public static DelegateExecution delegateExecution() {
    final DelegateExecution execution = mock(DelegateExecution.class, RETURNS_DEEP_STUBS);

    when(execution.getBpmnModelElementInstance().getElementType().getTypeName()).thenReturn("serviceTask");
    when(execution.getProcessDefinitionId()).thenReturn(PROCESS_DEFINITION_ID);
    when(execution.getEventName()).thenReturn("start");
    when(execution.getCurrentActivityId()).thenReturn("service1");

    return execution;
  }

  public static DelegateCaseExecution delegateCaseExecution() {
    final DelegateCaseExecution execution = mock(DelegateCaseExecution.class, RETURNS_DEEP_STUBS);

    when(execution.getCmmnModelElementInstance().getElementType().getTypeName()).thenReturn("serviceTask");
    when(execution.getCaseDefinitionId()).thenReturn(CASE_DEFINITION_ID);
    when(execution.getEventName()).thenReturn("start");
    when(execution.getActivityId()).thenReturn("service1");

    return execution;
  }

}
