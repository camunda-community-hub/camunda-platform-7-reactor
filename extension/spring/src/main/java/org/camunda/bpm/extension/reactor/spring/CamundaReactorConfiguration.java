package org.camunda.bpm.extension.reactor.spring;

import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.bus.EventBus;

@Configuration
public class CamundaReactorConfiguration {

  @Bean
  public CamundaEventBus camundaEventBus() {
    return new CamundaEventBus();
  }

  @Bean
  @Qualifier("camunda")
  public EventBus eventBus(final CamundaEventBus camundaEventBus) {
    return camundaEventBus.get();
  }

  @Bean
  public ReactorProcessEnginePlugin reactorProcessEnginePlugin(final CamundaEventBus camundaEventBus) {
    return new ReactorProcessEnginePlugin(camundaEventBus);
  }

}
