package org.camunda.bpm.extension.reactor;

import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.extension.reactor.event.DelegateCaseExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import org.camunda.bpm.extension.reactor.listener.SubscriberExecutionListener;
import org.camunda.bpm.extension.reactor.listener.SubscriberTaskListener;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import reactor.bus.EventBus;
import reactor.bus.selector.Selector;
import reactor.bus.selector.Selectors;

public final class CamundaReactor {

  public static final String CAMUNDA_TOPIC = "/camunda/{type}/{process}/{element}/{event}";

  public static class SubscribeTo {
    private final EventBus eventBus;

    SubscribeTo(EventBus eventBus) {
      this.eventBus = eventBus;
    }

    public void on(Selector topic, SubscriberTaskListener listener) {
      eventBus.on(topic, listener);
    }

    public void on(Selector topic, SubscriberExecutionListener listener) {
      eventBus.on(topic, listener);
    }

    public void on(Selector topic, TaskListener listener) {
      on(topic, SubscriberTaskListener.create(listener));
    }

    public void on(Selector topic, ExecutionListener listener) {
      eventBus.on(topic, SubscriberExecutionListener.create(listener));
    }

    public void on(SelectorBuilder topic, TaskListener listener) {
      on(topic.build(), SubscriberTaskListener.create(listener));
    }

    public void on(SelectorBuilder topic, ExecutionListener listener) {
      eventBus.on(topic.build(), SubscriberExecutionListener.create(listener));
    }
  }

  @Deprecated
  public static String key(final String process, final String element, final String event) {
    return SelectorBuilder.selector().process(process).element(element).event(event).key();
  }

  public static String key(final DelegateTask delegateTask) {
    return SelectorBuilder.selector(delegateTask).key();
  }

  public static String key(final DelegateExecution delegateExecution) {
    return SelectorBuilder.selector(delegateExecution).key();
  }

  public static String key(final DelegateCaseExecution delegateCaseExecution) {
    return SelectorBuilder.selector(delegateCaseExecution).key();
  }

  public static DelegateTaskEvent wrap(final DelegateTask delegateTask) {
    return new DelegateTaskEvent(delegateTask);
  }

  public static DelegateExecutionEvent wrap(final DelegateExecution delegateExecution) {
    return new DelegateExecutionEvent(delegateExecution);
  }

  public static DelegateCaseExecutionEvent wrap(final DelegateCaseExecution delegateCaseExecution) {
    return new DelegateCaseExecutionEvent(delegateCaseExecution);
  }

  public static Selector uri(String process, String element, String event) {
    return uri(key(process, element, event));
  }

  public static Selector uri(String topic) {
    return Selectors.uri(topic);
  }

  public static SubscribeTo subscribeTo(final EventBus eventBus) {
    return new SubscribeTo(eventBus);
  }

  public static EventBus eventBus(final ProcessEngine processEngine) {
    ProcessEngineConfigurationImpl configuration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
    final List<ProcessEnginePlugin> plugins = configuration.getProcessEnginePlugins();

    if (plugins != null) {
      for (ProcessEnginePlugin plugin : plugins) {
        if (plugin instanceof ReactorProcessEnginePlugin) {
          return ((ReactorProcessEnginePlugin)plugin).getEventBus();
        }
      }
    }
    throw new IllegalStateException("No eventBus found. Make sure the Reactor plugin is configured correctly.");
  }

  public static EventBus eventBus() {
    final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    if (processEngine == null) {
      throw new IllegalStateException("No processEngine registered.");
    }
    return eventBus(processEngine);
  }

  /**
   * Ugly hack, delegate task should contain processdefinitionKey.
   *
   * @param processDefinitionId the process definition id
   * @return process definition key
   */
  public static String processDefintionKey(String processDefinitionId) {
    return processDefinitionId.replaceAll("(\\w+):\\d+:\\d+", "$1");
  }

  /**
   * Yet anpother ugly hack, delegate task should contain caseDefinitionKey.
   *
   * @param caseDefinitionId
   * @return case definition key
   */
  public static String caseDefintionKey(String caseDefinitionId) {
    return caseDefinitionId.replaceAll("(\\w+):\\d+:\\d+", "$1");
  }

  private CamundaReactor() {
    // util class
  }
}
