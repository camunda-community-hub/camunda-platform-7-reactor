package org.camunda.bpm.extension.reactor.spring;

import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(CamundaReactorConfiguration.class)
public class CamundaReactorAutoConfiguration {

  // empty

}
