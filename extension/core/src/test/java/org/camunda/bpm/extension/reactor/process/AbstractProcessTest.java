package org.camunda.bpm.extension.reactor.process;

import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.ReactorProcessEngineConfiguration;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.junit.Before;
import org.junit.Rule;

public class AbstractProcessTest {
	@Rule
	public final ProcessEngineRule processEngineRule = ReactorProcessEngineConfiguration
			.buildRule();

	protected CamundaEventBus eventBus;

	@Before
	public void init() {
		eventBus = CamundaReactor.eventBus();
	}

}
