package org.camunda.bpm.extension.example.reactor;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import static org.slf4j.LoggerFactory.getLogger;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.camunda.bpm.extension.reactor.event.DelegateEventConsumer;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;

@Deployment(resources = "ProcessA.bpmn")
public class ProcessATest {
  static {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
  }

  private final Logger logger = getLogger(this.getClass());
  @Rule
  public final ProcessEngineRule processEngineRule = new ProcessEngineRule(Setup.CONFIGURATION.buildProcessEngine());

  @Test
  public void run_process() {
    Setup.init();

    CamundaReactor.eventBus().register(SelectorBuilder.selector(), new DelegateEventConsumer() {
      @Override
      public void accept(DelegateEvent delegateEvent) {
        logger.debug(delegateEvent.toString());
      }
    });

    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("process_a");

    assertThat(processInstance).isWaitingAt("task_a");
    assertThat(task()).hasDueDate(ProcessA.DUE_DATE);


    Task t = taskService().createTaskQuery().singleResult();
    assertThat(taskQuery().taskId(t.getId()).taskCandidateGroup(ProcessA.GROUP_1).includeAssignedTasks().singleResult()).isNotNull();
    assertThat(taskQuery().taskId(t.getId()).taskCandidateGroup(ProcessA.GROUP_2).includeAssignedTasks().singleResult()).isNotNull();
    assertThat(taskQuery().taskId(t.getId()).taskCandidateGroup(ProcessA.GROUP_3).includeAssignedTasks().singleResult()).isNotNull();


    //assertThat(task()).hasCandidateGroup(ProcessA.GROUP_1);
    //assertThat(task()).hasCandidateGroup(ProcessA.GROUP_2);
    //assertThat(task()).hasCandidateGroup(ProcessA.GROUP_2);
    //assertThat(task()).isAssignedTo("me");


  }
}

