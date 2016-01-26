package org.camunda.bpm.extension.reactor.plugin;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cmmn.transformer.CmmnTransformListener;
import org.camunda.bpm.extension.reactor.CamundaEventBus;
import reactor.bus.EventBus;
import reactor.core.dispatch.SynchronousDispatcher;

public class ReactorProcessEnginePlugin extends AbstractProcessEnginePlugin {

  public static final EventBus CAMUNDA_EVENTBUS = new CamundaEventBus();

  private final EventBus eventBus;

  /**
   * Initializes synchronous eventBus.
   */
  public ReactorProcessEnginePlugin() {
    this(CAMUNDA_EVENTBUS);
  }

  public ReactorProcessEnginePlugin(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void preInit(final ProcessEngineConfigurationImpl processEngineConfiguration) {
    customPreBPMNParseListeners(processEngineConfiguration).add(new ReactorBpmnParseListener(eventBus));
    customPreCMMNTransformListeners(processEngineConfiguration).add(new ReactorCmmnTransformListener(eventBus));
  }

  public EventBus getEventBus() {
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
