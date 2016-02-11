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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Malte.Soerensen
 */
@Deployment(resources = "Case.cmmn")
public class CaseTest {

  @Rule
  public final ProcessEngineRule processEngineRule = new ProcessEngineRule(Setup.processEngine());

  @Before
  public void init() {
    Setup.init();
  }

  @Test
  public void assigneToMe() {
    CaseService caseService = processEngine().getCaseService();
    CaseInstance caseInstance = caseService.createCaseInstanceByKey("Case");

    Task task = taskService().createTaskQuery().caseInstanceId(caseInstance.getId()).singleResult();
    assertThat(task).isAssignedTo("me");

    CaseExecution caseExecution = caseService.createCaseExecutionQuery().activityId("_ab60407c-449e-4048-ba9c-01bb59f2f095").singleResult();
    caseService.completeCaseExecution(caseExecution.getId());
  }
}
