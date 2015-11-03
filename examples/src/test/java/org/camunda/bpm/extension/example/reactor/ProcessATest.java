package org.camunda.bpm.extension.example.reactor;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.assertions.ProcessEngineTests;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;

public class ProcessATest {
  static {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
  }

  @Rule
  public final ProcessEngineRule processEngineRule = new ProcessEngineRule(ProcessA.CONFIGURATION.buildProcessEngine());

  @Test
  @Deployment(resources = "ProcessA.bpmn")
  public void run_process() {
     new ProcessA().init();


    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("process_a");

    assertThat(processInstance).isWaitingAt("task_a");
    assertThat(task()).hasDueDate(ProcessA.DUE_DATE);
    

    assertThat(task()).hasCandidateGroup(ProcessA.GROUP_1);
    assertThat(task()).hasCandidateGroup(ProcessA.GROUP_2);
    assertThat(task()).hasCandidateGroup(ProcessA.GROUP_3);
    assertThat(task()).isAssignedTo("me");

    
  }
}

