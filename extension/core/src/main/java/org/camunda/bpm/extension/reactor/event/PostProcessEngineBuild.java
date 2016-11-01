package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.ProcessEngine;


public class PostProcessEngineBuild extends ProcessEnginePluginEvent<ProcessEngine, PostProcessEngineBuild> {

  public static final String METHOD = "postProcessEngineBuild";

  protected PostProcessEngineBuild(final ProcessEngine processEngine) {
    super(processEngine, PostProcessEngineBuild.class);
  }
}
