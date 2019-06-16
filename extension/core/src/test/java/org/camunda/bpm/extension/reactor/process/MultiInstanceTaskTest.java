package org.camunda.bpm.extension.reactor.process;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.assertions.ProcessEngineTests;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.ReactorProcessEngineConfiguration;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.camunda.bpm.extension.reactor.event.DelegateEventConsumer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.reset;

public class MultiInstanceTaskTest {

  @Rule
  public final ProcessEngineRule processEngineRule = ReactorProcessEngineConfiguration.buildRule();

  private CamundaEventBus eventBus;

  @Before
  public void setUp() {
    eventBus = CamundaReactor.eventBus();
  }

  @After
  public void tearDown() {
    reset();
  }

  @Test
  @Deployment(resources="MultiInstanceTaskProcess.bpmn")
  public void start_multi_instance_process() {

    eventBus.register(SelectorBuilder.selector(), new DelegateEventConsumer() {

      @Override
      public void accept(final DelegateEvent t) {
        System.out.println(t);
      }
    });

    ProcessEngineTests.runtimeService().startProcessInstanceByKey("multiInstanceTaskProcess", Variables.createVariables().putValue("items", Arrays.asList("foo","bar")));


  }

}
