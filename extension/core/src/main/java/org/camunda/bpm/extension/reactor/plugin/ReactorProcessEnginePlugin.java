package org.camunda.bpm.extension.reactor.plugin;

import org.camunda.bpm.engine.ProcessEngine;
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

public class ReactorProcessEnginePlugin extends AbstractProcessEnginePlugin {

  public static class Configuration {

    public String getImplementationVersion() {
      return ProcessEngine.class.getPackage().getImplementationVersion();
    }

    public int getMajorMinorVersion() {
      return Integer.valueOf(getImplementationVersion()
        .replaceAll("\\.","")
        .substring(0,2));
    }

    /**
     * Transaction threw a CCE in 7.4 so it is now only activated for version >= 7.5.x.
     *
     * @return <code>true</code>  if eventbus should be registered on transaction element.
     */
    public boolean isActivateTransaction() {
      return getMajorMinorVersion() >= 75;
    }
  }

  private final CamundaEventBus eventBus;

  private Configuration configuration = new Configuration();

  /**
   * Default constructor for bean initialization. Uses <code>new CamundaEventBus()</code>.
   *
   * @see #ReactorProcessEnginePlugin(CamundaEventBus)
   */
  public ReactorProcessEnginePlugin() {
    this(new CamundaEventBus());
  }

  public ReactorProcessEnginePlugin(final CamundaEventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void preInit(final ProcessEngineConfigurationImpl processEngineConfiguration) {

    customPreBPMNParseListeners(processEngineConfiguration)
      .add(new RegisterAllBpmnParseListener(eventBus.getTaskListener(), eventBus.getExecutionListener(), configuration));

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
