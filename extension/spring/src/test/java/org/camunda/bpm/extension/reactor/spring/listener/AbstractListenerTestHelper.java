package org.camunda.bpm.extension.reactor.spring.listener;

import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AbstractListenerTestHelper {

  @Autowired
  protected CamundaEventBus camundaEventBus;


  public static DelegateTask delegateTask() {
    return delegateTask("task1", "process:1:1", "create");
  }

  public static DelegateTask delegateTask(String taskName, String processDefinitionId, String eventName) {
    final DelegateTask task = mock(DelegateTask.class, RETURNS_DEEP_STUBS);
    when(task.getBpmnModelElementInstance().getElementType().getTypeName()).thenReturn("userTask");

    when(task.getTaskDefinitionKey()).thenReturn(taskName);
    when(task.getProcessDefinitionId()).thenReturn(processDefinitionId);
    when(task.getEventName()).thenReturn(eventName);

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
