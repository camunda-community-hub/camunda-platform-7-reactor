package org.camunda.bpm.extension.example.reactor;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.taskService;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.runtime.CaseExecution;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * @author Malte.Soerensen
 */
public class CaseBTest {
  static {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
  }

  @Rule
  public final ProcessEngineRule processEngineRule = new ProcessEngineRule(Setup.processEngine);

  @Test
  @Deployment(resources = "CaseB.cmmn")
  public void run_case() {
    Setup.init();

    CaseService caseService = processEngine().getCaseService();
    CaseInstance caseInstance = caseService.createCaseInstanceByKey("Case_c4948022-13c8-4f70-ab8a-ede29c44c298");

    Task task = taskService().createTaskQuery().caseInstanceId(caseInstance.getId()).singleResult();

    assertThat(task).isAssignedTo("me");

    CaseExecution caseExecution = caseService.createCaseExecutionQuery().activityId("_ab60407c-449e-4048-ba9c-01bb59f2f095").singleResult();
    caseService.completeCaseExecution(caseExecution.getId());


  }
}
