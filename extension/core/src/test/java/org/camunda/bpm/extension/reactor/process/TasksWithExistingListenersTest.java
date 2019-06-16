package org.camunda.bpm.extension.reactor.process;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.reactor.ReactorProcessEngineConfiguration;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.reset;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;
import static org.camunda.bpm.engine.test.mock.Mocks.register;

@Deployment(resources="ProcessWithExistingListeners.bpmn")
public class TasksWithExistingListenersTest {

  private final CamundaEventBus eventBus = new CamundaEventBus();

  @Rule
  public final ProcessEngineRule processEngineRule = ReactorProcessEngineConfiguration.buildRule(eventBus);

  @After
  public void tearDown() {
    reset();
  }

  @Test
  public void reactor_listener_is_called_last() {
    final TestTaskCreateListener customTestTaskCreateListener = new TestTaskCreateListener();
    final TestTaskCreateListener reactorTestTaskCreateListener = new TestTaskCreateListener();

    //Custom Listener in BPMN
    register("testTaskCreateListener", customTestTaskCreateListener);

    //Listener via reactor bus
    eventBus.register(SelectorBuilder.selector(reactorTestTaskCreateListener), reactorTestTaskCreateListener);

    runtimeService().startProcessInstanceByKey("processWithExistingListeners");

    assertThat(customTestTaskCreateListener.calledTime).isNotNull();
    assertThat(reactorTestTaskCreateListener.calledTime).isNotNull();
    assertThat(customTestTaskCreateListener.calledTime.before(reactorTestTaskCreateListener.calledTime)).isTrue();
  }



}
