package org.camunda.bpm.extension.reactor.process;

import java.util.Arrays;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.assertions.ProcessEngineTests;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.camunda.bpm.extension.reactor.event.DelegateEventConsumer;
import org.junit.Test;

public class MultiInstanceTaskTest extends AbstractProcessTest {
  @Test
  @Deployment(resources="MultiInstanceTaskProcess.bpmn")
  public void start_multi_instance_process() throws Exception {

    eventBus.register(SelectorBuilder.selector(), new DelegateEventConsumer() {

      @Override
      public void accept(DelegateEvent t) {
        System.out.println(t);
      }
    });

    ProcessEngineTests.runtimeService().startProcessInstanceByKey("multiInstanceTaskProcess", Variables.createVariables().putValue("items", Arrays.asList("foo","bar")));


  }

}
