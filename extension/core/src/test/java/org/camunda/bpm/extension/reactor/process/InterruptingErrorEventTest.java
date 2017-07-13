package org.camunda.bpm.extension.reactor.process;


import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.Before;
import org.junit.Test;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.repositoryService;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;

public class InterruptingErrorEventTest extends AbstractProcessTest {

  public static final String KEY = "InterruptingErrorEventTest";

  public static class ServiceDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
      throw new BpmnError("errorCode");
    }
  }

  @Before
  public void setUp() throws Exception {

    BpmnModelInstance process = Bpmn.createExecutableProcess(KEY)
      .startEvent()
      .serviceTask("service").camundaDelegateExpression("serviceDelegate").boundaryEvent("error").error("errorCode")
      .endEvent()
      .done();

    processEngineRule.manageDeployment(repositoryService().createDeployment()
      .addModelInstance(KEY + ".bpmn", process)
      .deploy());

    Mocks.register("serviceDelegate", new ServiceDelegate());
  }

  @Test
  public void name() throws Exception {
    runtimeService().startProcessInstanceByKey(KEY);

  }
}
