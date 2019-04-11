package org.camunda.bpm.extension.reactor.process;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.ReactorProcessEngineConfiguration;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;
import static org.camunda.bpm.engine.test.mock.Mocks.register;

@Deployment(resources="ProcessWithExistingListeners.bpmn")
public class TasksWithExistingListenersTest {

  @Rule
  public final ProcessEngineRule processEngineRule = ReactorProcessEngineConfiguration.buildRule();

  private CamundaEventBus eventBus;

  @Before
  public void init() {
    eventBus = CamundaReactor.eventBus();
  }

  @Test
  public void reactor_listener_is_called_second() {
    final TaskCreateListener customTaskCreateListener = new TaskCreateListener();
    final TaskCreateListener reactorTaskCreateListener = new TaskCreateListener();

    //Custom Listener in BPMN
    register("taskCreateListener", customTaskCreateListener);

    //Listener via reactor bus
    eventBus.register(SelectorBuilder.selector(reactorTaskCreateListener), reactorTaskCreateListener);

    for(int i = 0; i < 100; i++) {
      runtimeService().startProcessInstanceByKey("processWithExistingListeners");

      assertThat(customTaskCreateListener.calledTime).isNotNull();
      assertThat(reactorTaskCreateListener.calledTime).isNotNull();
      assertThat(customTaskCreateListener.calledTime.before(reactorTaskCreateListener.calledTime)).isTrue();
    }

  }

  @CamundaSelector(type = "userTask", event = TaskListener.EVENTNAME_CREATE)
  static class TaskCreateListener implements TaskListener {
    Date calledTime;

    @Override
    public void notify(final DelegateTask delegateTask) {
      calledTime = new Date();
      try {
        Thread.sleep(1);
      } catch (final InterruptedException e) { }
    }
  }

}
