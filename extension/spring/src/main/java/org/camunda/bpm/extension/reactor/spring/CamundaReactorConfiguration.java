package org.camunda.bpm.extension.reactor.spring;

import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaReactorConfiguration {

  @Bean
  public CamundaEventBus camundaEventBus() {
    return new CamundaEventBus();
  }

  @Bean
  public ReactorProcessEnginePlugin reactorProcessEnginePlugin(final CamundaEventBus camundaEventBus) {
    return new ReactorProcessEnginePlugin(camundaEventBus);
  }

}
