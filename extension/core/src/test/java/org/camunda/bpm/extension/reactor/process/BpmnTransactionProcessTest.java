package org.camunda.bpm.extension.reactor.process;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.camunda.bpm.extension.reactor.event.DelegateEventConsumer;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;

@Ignore("works in IDE but not with maven")
@Deployment(resources = "BpmnTransactionProcess.bpmn")
public class BpmnTransactionProcessTest extends AbstractProcessTest {

  @Test
  public void start_bpmn_transaction_process() throws Exception {

    final StringBuffer b = new StringBuffer();

    eventBus.register(SelectorBuilder.selector().type("transaction"),
      new DelegateEventConsumer() {

        @Override
        public void accept(DelegateEvent t) {
          b.append(t.toString());
        }
      });

    runtimeService().startProcessInstanceByKey("BpmnTransactionProcess");

    System.out.println(ReactorProcessEnginePlugin.CONFIGURATION);

    String version = ReactorProcessEnginePlugin.CONFIGURATION.getImplementationVersion();

    if (version.startsWith("7.4")) {
      assertThat(b.toString()).isEmpty();
    } else if (version.startsWith("7.5")) {
      assertThat(b.toString()).isNotEmpty();
    } else if (version.startsWith("7.6")) {
      assertThat(b.toString()).isNotEmpty();
    }
  }

}
