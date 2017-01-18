package org.camunda.bpm.extension.reactor.plugin.listener;

import static org.assertj.core.api.Assertions.assertThat;

import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.impl.spi.transform.DmnTransformer;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.reactor.ReactorProcessEngineConfiguration;
import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;
import org.camunda.bpm.extension.reactor.event.DmnDeployedEvent;
import org.camunda.bpm.extension.reactor.model.DecisionDto;
import org.camunda.bpm.extension.reactor.util.AtomicEventConsumer;
import org.junit.Rule;
import org.junit.Test;

public class DmnDeployedListenerTest {

  private final SynchronousEventBus eventBus = new SynchronousEventBus();

  private final ProcessEnginePlugin registerListener = new AbstractProcessEnginePlugin() {
    @Override
    public void postInit(final ProcessEngineConfigurationImpl processEngineConfiguration) {
      final DefaultDmnEngineConfiguration dmnEngineConfiguration = processEngineConfiguration.getDmnEngineConfiguration();
      final DmnTransformer transformer = dmnEngineConfiguration.getTransformer();
      transformer.getTransformListeners().add(new DmnDeployedListener(eventBus));
    }
  };

  @Rule
  public ProcessEngineRule camunda = new ReactorProcessEngineConfiguration(registerListener).buildProcessEngineRule();

  @Test
  public void notify_on_deployment() throws Exception {
    AtomicEventConsumer<DmnDeployedEvent> eventConsumer = new AtomicEventConsumer<>();

    DmnDeployedEvent.register(eventBus, eventConsumer);

    camunda.getRepositoryService().createDeployment()
      .addClasspathResource("FooBarDiagram.dmn")
      .deploy();

    assertThat(eventConsumer.get()).isNotNull();
    DecisionDto dto = eventConsumer.get().getData();

    assertThat(dto.getKey()).isEqualTo("decision_b");
    assertThat(dto.getName()).isEqualTo("B");
  }
}
