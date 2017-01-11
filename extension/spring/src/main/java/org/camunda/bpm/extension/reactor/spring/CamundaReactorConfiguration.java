package org.camunda.bpm.extension.reactor.spring;

import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.bus.EventBus;

@Configuration
public class CamundaReactorConfiguration {

  @Bean
  public SynchronousEventBus synchronousEventBus() {
    return new SynchronousEventBus();
  }

  @Bean
  public CamundaEventBus camundaEventBus(final SynchronousEventBus eventBus) {
    return new CamundaEventBus(eventBus);
  }

  @Bean
  @Qualifier("camunda")
  public EventBus eventBus(final SynchronousEventBus eventBus) {
    return eventBus;
  }

  @Bean
  public ReactorProcessEnginePlugin reactorProcessEnginePlugin(final CamundaEventBus camundaEventBus) {
    return new ReactorProcessEnginePlugin(camundaEventBus);
  }

}
