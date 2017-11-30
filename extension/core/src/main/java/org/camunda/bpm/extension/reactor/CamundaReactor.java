package org.camunda.bpm.extension.reactor;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.impl.cfg.CompositeProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.camunda.bpm.extension.reactor.event.DelegateCaseExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class CamundaReactor {

  public static final String CAMUNDA_TOPIC = "/camunda/{context}/{type}/{process}/{element}/{event}";

  public static DelegateTaskEvent wrap(final DelegateTask delegateTask) {
    return new DelegateTaskEvent(delegateTask);
  }

  public static DelegateExecutionEvent wrap(final DelegateExecution delegateExecution) {
    return new DelegateExecutionEvent(delegateExecution);
  }

  private static Supplier<IllegalStateException> illegalState(String msg) {
    return () -> new IllegalStateException(msg);
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
    final ProcessEngineConfigurationImpl configuration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
    final List<ProcessEnginePlugin> plugins = Optional.ofNullable(configuration.getProcessEnginePlugins()).orElse(Collections.EMPTY_LIST);

    final Function<List<ProcessEnginePlugin>, Optional<CamundaEventBus>> filterReactorPlugin = l -> l.stream()
      .filter(plugin -> plugin instanceof ReactorProcessEnginePlugin)
      .map(ReactorProcessEnginePlugin.class::cast)
      .map(ReactorProcessEnginePlugin::getEventBus)
      .findFirst();

    Optional<CamundaEventBus> reactorProcessEnginePlugin = filterReactorPlugin.apply(plugins);
    if (reactorProcessEnginePlugin.isPresent()) {
      return reactorProcessEnginePlugin.get();
    }

    return plugins.stream()
      .filter(plugin -> plugin instanceof CompositeProcessEnginePlugin)
      .map(CompositeProcessEnginePlugin.class::cast)
      .map(CompositeProcessEnginePlugin::getPlugins)
      .map(filterReactorPlugin)
      .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
      .findFirst()
      .orElseThrow(illegalState("No eventBus found. Make sure the Reactor plugin is configured correctly."));
  }

  /**
   * Gets EventBus from registered default process engine.
   *
   * @return the camunda eventBus
   */
  public static CamundaEventBus eventBus() {
    return eventBus(Optional.ofNullable(ProcessEngines.getDefaultProcessEngine())
      .orElseThrow(illegalState("No processEngine registered.")));
  }

  public static ReactorProcessEnginePlugin plugin() {
    return plugin(new CamundaEventBus());
  }

  public static ReactorProcessEnginePlugin plugin(final CamundaEventBus camundaEventBus) {
    return new ReactorProcessEnginePlugin(camundaEventBus);
  }

  private CamundaReactor() {
    // util class
  }
}
