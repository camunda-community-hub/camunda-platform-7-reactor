package org.camunda.bpm.extension.example.reactor;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@Deployment(resources = "Process.bpmn")
public class ProcessTest {

  @Rule
  public final ProcessEngineRule processEngineRule = new ProcessEngineRule(Setup.processEngine());

  @Before
  public void init() {
    Setup.init();
  }

  @Test
  public void measureKpi() {
    runtimeService().startProcessInstanceByKey("process");

    assertThat(KpiService.getInstance().getKpi("step1"), is(1L));
  }

}
