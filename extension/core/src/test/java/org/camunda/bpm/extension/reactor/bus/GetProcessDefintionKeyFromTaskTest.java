package org.camunda.bpm.extension.reactor.bus;

import static org.assertj.core.api.Assertions.assertThat;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.reactor.ReactorProcessEngineConfiguration;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class GetProcessDefintionKeyFromTaskTest {

  public static final String NAME = "GetProcessDefintionKeyFromTaskTest";
  public static final String PROCESS_RESOURCE= NAME + ".bpmn";
  public static final String PROCESS_KEY = NAME + "_very_long_defintion_key_forces_camunda_to_alter_the_processDefinitionId_schema";
  private final CamundaEventBus camundaEventBus = new CamundaEventBus();

  private final ReactorProcessEngineConfiguration configuration = new ReactorProcessEngineConfiguration(camundaEventBus);

  @Rule
  public final ProcessEngineRule camunda = new ProcessEngineRule(configuration.buildProcessEngine());

  @Before
  public void setUp() throws Exception {
    camunda.manageDeployment(camunda.getRepositoryService().createDeployment()
      .addModelInstance(PROCESS_RESOURCE, Bpmn.createExecutableProcess(PROCESS_KEY)
        .startEvent()
        .userTask("task")
        .endEvent()
        .done())
      .deploy());
  }

  @Test
  public void listen_to_taskCreate() throws Exception {
    TheTaskListener taskListener = new TheTaskListener();
    camundaEventBus.register(taskListener);

    camunda.getRuntimeService().startProcessInstanceByKey(PROCESS_KEY);

    assertThat(taskListener.accessed).isTrue();
  }

  @CamundaSelector(event = TaskListener.EVENTNAME_CREATE, process = PROCESS_KEY)
  public static class TheTaskListener implements TaskListener {

    private boolean accessed = false;

    @Override
    public void notify(DelegateTask delegateTask) {
      accessed = true;
    }
  }
}
