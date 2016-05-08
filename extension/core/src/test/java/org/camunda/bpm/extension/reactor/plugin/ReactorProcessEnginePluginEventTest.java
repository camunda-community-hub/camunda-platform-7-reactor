package org.camunda.bpm.extension.reactor.plugin;


import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.extension.reactor.ReactorProcessEngineConfiguration;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactorProcessEnginePluginEventTest {

  private final CamundaEventBus eventBus = new CamundaEventBus();

  private final ProcessEngineConfigurationImpl processEngineConfiguration = new ReactorProcessEngineConfiguration(eventBus);

  @Test
  public void apply_plugin_via_event() throws Exception {
    final Map<String, Object> collect = new HashMap<>();

    eventBus.register(new ProcessEnginePlugin() {
      @Override
      public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        collect.put("preInit", processEngineConfiguration);
      }

      @Override
      public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        collect.put("postInit", processEngineConfiguration);
      }

      @Override
      public void postProcessEngineBuild(ProcessEngine processEngine) {
        collect.put("postProcessEngineBuild", processEngine);
      }
    });

    processEngineConfiguration.buildProcessEngine();

    assertThat(collect).hasSize(3).containsKeys("preInit", "postInit", "postProcessEngineBuild");
    assertThat(collect.values()).doesNotContainNull();
  }
}
