package org.camunda.bpm.extension.reactor.plugin;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cmmn.transformer.CmmnTransformListener;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.event.ProcessEnginePluginEvent;
import org.camunda.bpm.extension.reactor.plugin.parse.RegisterAllBpmnParseListener;
import org.camunda.bpm.extension.reactor.plugin.parse.RegisterAllCmmnTransformListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReactorProcessEnginePlugin extends AbstractProcessEnginePlugin {


  private final CamundaEventBus eventBus;

  public ReactorProcessEnginePlugin(final CamundaEventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void preInit(final ProcessEngineConfigurationImpl processEngineConfiguration) {

    customPreBPMNParseListeners(processEngineConfiguration)
      .add(new RegisterAllBpmnParseListener(eventBus.getTaskListener(), eventBus.getExecutionListener()));

    customPreCMMNTransformListeners(processEngineConfiguration).add(
      new RegisterAllCmmnTransformListener(eventBus.getTaskListener(), eventBus.getCaseExecutionListener()));

    eventBus.notify(ProcessEnginePluginEvent.preInit(processEngineConfiguration));
  }

  @Override
  public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
    eventBus.notify(ProcessEnginePluginEvent.postInit(processEngineConfiguration));
  }

  @Override
  public void postProcessEngineBuild(ProcessEngine processEngine) {
    eventBus.notify(ProcessEnginePluginEvent.postProcessEngineBuild(processEngine));
  }

  public CamundaEventBus getEventBus() {
    return eventBus;
  }

  private static List<BpmnParseListener> customPreBPMNParseListeners(final ProcessEngineConfigurationImpl processEngineConfiguration) {
    if (processEngineConfiguration.getCustomPreBPMNParseListeners() == null) {
      processEngineConfiguration.setCustomPreBPMNParseListeners(new ArrayList<BpmnParseListener>());
    }
    return processEngineConfiguration.getCustomPreBPMNParseListeners();
  }

  private static List<CmmnTransformListener> customPreCMMNTransformListeners(final ProcessEngineConfigurationImpl processEngineConfiguration) {
    if (processEngineConfiguration.getCustomPreCmmnTransformListeners() == null) {
      processEngineConfiguration.setCustomPreCmmnTransformListeners(new ArrayList<CmmnTransformListener>());
    }
    return processEngineConfiguration.getCustomPreCmmnTransformListeners();
  }
}
