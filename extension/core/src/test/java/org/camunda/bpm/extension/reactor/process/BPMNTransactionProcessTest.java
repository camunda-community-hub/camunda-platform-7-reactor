package org.camunda.bpm.extension.reactor.process;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.assertions.ProcessEngineTests;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.camunda.bpm.extension.reactor.event.DelegateEventConsumer;
import org.junit.Test;

public class BPMNTransactionProcessTest extends AbstractProcessTest {

	@Test
	@Deployment(resources = "BPMNTransactionProcess.bpmn")
	public void start_bpmn_transaction_process() throws Exception {

		eventBus.register(SelectorBuilder.selector(),
				new DelegateEventConsumer() {

					@Override
					public void accept(DelegateEvent t) {
						System.out.println(t);
					}
				});

		ProcessEngineTests.runtimeService().startProcessInstanceByKey(
				"BPMNTransactionProcess");

	}

}
