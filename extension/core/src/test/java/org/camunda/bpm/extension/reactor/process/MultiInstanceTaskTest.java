package org.camunda.bpm.extension.reactor.process;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Arrays;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.assertions.ProcessEngineTests;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.camunda.bpm.extension.reactor.ReactorProcessEngineConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import reactor.bus.EventBus;
import reactor.fn.Consumer;

public class MultiInstanceTaskTest {


  @Rule
  public final ProcessEngineRule processEngineRule = ReactorProcessEngineConfiguration.buildRule();

  private EventBus eventBus;

  @Before
  public void init() {
    eventBus = CamundaReactor.eventBus();
  }

  @Test
  @Deployment(resources="MultiInstanceTaskProcess.bpmn")
  public void start_multi_instance_process() throws Exception {

    eventBus.on(SelectorBuilder.selector().build(), new Consumer<DelegateEvent>() {

      @Override
      public void accept(DelegateEvent t) {
        System.out.println(t);
      }
    });

    ProcessEngineTests.runtimeService().startProcessInstanceByKey("multiInstanceTaskProcess", Variables.createVariables().putValue("items", Arrays.asList("foo","bar")));


  }

}
