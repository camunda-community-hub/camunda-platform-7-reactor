package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;


public class PostInitEvent extends ProcessEnginePluginEvent<ProcessEngineConfigurationImpl, PostInitEvent> {

  public static final String METHOD = "postInit";

  protected PostInitEvent(final ProcessEngineConfigurationImpl processEngineConfiguration) {
    super(processEngineConfiguration, PostInitEvent.class);
  }
}
