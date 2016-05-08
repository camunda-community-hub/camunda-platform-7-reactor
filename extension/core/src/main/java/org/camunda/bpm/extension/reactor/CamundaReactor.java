package org.camunda.bpm.extension.reactor;

import org.camunda.bpm.container.RuntimeContainerDelegate;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.camunda.bpm.extension.reactor.event.DelegateCaseExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;

import java.util.List;

public final class CamundaReactor {

  public static final String CAMUNDA_TOPIC = "/camunda/{context}/{type}/{process}/{element}/{event}";

  public static DelegateTaskEvent wrap(final DelegateTask delegateTask) {
    return new DelegateTaskEvent(delegateTask);
  }

  public static DelegateExecutionEvent wrap(final DelegateExecution delegateExecution) {
    return new DelegateExecutionEvent(delegateExecution);
  }

  public static DelegateCaseExecutionEvent wrap(final DelegateCaseExecution delegateCaseExecution) {
    return new DelegateCaseExecutionEvent(delegateCaseExecution);
  }

  public static SelectorBuilder selector() {
    return SelectorBuilder.selector();
  }

  /**
   * Gets EventBus from given process engine via plugin.
   *
   * @param processEngine the process engine
   * @return the camunda eventBus
   */
  public static CamundaEventBus eventBus(final ProcessEngine processEngine) {
    ProcessEngineConfigurationImpl configuration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
    final List<ProcessEnginePlugin> plugins = configuration.getProcessEnginePlugins();

    if (plugins != null) {
      for (ProcessEnginePlugin plugin : plugins) {
        if (plugin instanceof ReactorProcessEnginePlugin) {
          return ((ReactorProcessEnginePlugin) plugin).getEventBus();
        }
      }
    }
    throw new IllegalStateException("No eventBus found. Make sure the Reactor plugin is configured correctly.");
  }

  /**
   * Gets EventBus from registered default process engine.
   *
   * @return the camunda eventBus
   */
  public static CamundaEventBus eventBus() {
    final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    if (processEngine == null) {
      throw new IllegalStateException("No processEngine registered.");
    }
    return eventBus(processEngine);
  }

  public static ReactorProcessEnginePlugin plugin() {
    return plugin(ReactorProcessEnginePlugin.CAMUNDA_EVENTBUS);
  }

  public static ReactorProcessEnginePlugin plugin(CamundaEventBus camundaEventBus) {
    return new ReactorProcessEnginePlugin();
  }

  private CamundaReactor() {
    // util class
  }
}
