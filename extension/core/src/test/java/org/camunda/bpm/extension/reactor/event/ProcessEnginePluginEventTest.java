package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;

public class ProcessEnginePluginEventTest {

  @Rule
  public final MockitoRule mockito = MockitoJUnit.rule();

  @Mock
  private ProcessEngineConfigurationImpl processEngineConfiguration;

  @Mock
  private ProcessEngine processEngine;

  private final CamundaEventBus camundaEventBus = new CamundaEventBus();

  @Test
  public void fire_preInit() throws Exception {
    camundaEventBus.register(new AbstractProcessEnginePlugin() {
      @Override
      public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        processEngineConfiguration.getBeans();
      }
    });

    camundaEventBus.notify(ProcessEnginePluginEvent.preInit(processEngineConfiguration));

    verify(processEngineConfiguration).getBeans();
  }

  @Test
  public void fire_postInit() throws Exception {

    camundaEventBus.register(new AbstractProcessEnginePlugin() {
      @Override
      public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        processEngineConfiguration.getBeans();
      }
    });

    camundaEventBus.notify(ProcessEnginePluginEvent.postInit(processEngineConfiguration));

    verify(processEngineConfiguration).getBeans();
  }


  @Test
  public void fire_postEngineBuild() throws Exception {

    camundaEventBus.register(new AbstractProcessEnginePlugin() {
      @Override
      public void postProcessEngineBuild(ProcessEngine processEngine) {
        processEngine.getName();
      }
    });

    camundaEventBus.notify(ProcessEnginePluginEvent.postProcessEngineBuild(processEngine));

    verify(processEngine).getName();
  }
}
