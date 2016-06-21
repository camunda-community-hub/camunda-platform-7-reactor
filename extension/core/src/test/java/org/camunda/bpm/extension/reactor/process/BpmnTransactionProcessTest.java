package org.camunda.bpm.extension.reactor.process;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.camunda.bpm.extension.reactor.event.DelegateEventConsumer;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;

@Deployment(resources = "BpmnTransactionProcess.bpmn")
public class BpmnTransactionProcessTest extends AbstractProcessTest {

  private final ReactorProcessEnginePlugin.Configuration configuration = new ReactorProcessEnginePlugin.Configuration();

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

    if (configuration.getImplementationVersion().startsWith("7.4")) {
      assertThat(b.toString()).isEmpty();
    } else if (configuration.getImplementationVersion().startsWith("7.5")) {
      assertThat(b.toString()).isNotEmpty();
    } else if (configuration.getImplementationVersion().startsWith("7.6")) {
      assertThat(b.toString()).isNotEmpty();
    }
  }

}
