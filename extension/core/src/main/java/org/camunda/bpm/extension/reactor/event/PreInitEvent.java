package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

public class PreInitEvent extends ProcessEnginePluginEvent<ProcessEngineConfigurationImpl, PreInitEvent> {

  public static final String METHOD = "preInit";

  protected PreInitEvent(final ProcessEngineConfigurationImpl processEngineConfiguration) {
    super(processEngineConfiguration, PreInitEvent.class);
  }
}
