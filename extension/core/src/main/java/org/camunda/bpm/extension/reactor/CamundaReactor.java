package org.camunda.bpm.extension.reactor;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.camunda.bpm.extension.reactor.event.DelegateExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import org.camunda.bpm.extension.reactor.listener.SubscriberExecutionListener;
import org.camunda.bpm.extension.reactor.listener.SubscriberTaskListener;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.slf4j.LoggerFactory;
import reactor.bus.EventBus;
import reactor.bus.selector.Selector;
import reactor.bus.selector.Selectors;
import reactor.fn.Consumer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

  public static String topic(final String process, final String element, final String event) {
    return SelectorBuilder.selector().process(process).element(element).event(event).createTopic();
  }

  public static String topic(final DelegateTask delegateTask) {
    return SelectorBuilder.selector(delegateTask).createTopic();
  }

  public static String topic(final DelegateExecution delegateExecution) {
    return SelectorBuilder.selector(delegateExecution).createTopic();
  }

  public static DelegateTaskEvent wrap(final DelegateTask delegateTask) {
    return new DelegateTaskEvent(delegateTask);
  }

  public static DelegateExecutionEvent wrap(final DelegateExecution delegateExecution) {
    return new DelegateExecutionEvent(delegateExecution);
  }

  public static Selector uri(String process, String element, String event) {
    return uri(topic(process, element, event));
  }

  public static Selector uri(String topic) {
    return Selectors.uri(topic);
  }

  public static SubscribeTo subscribeTo(final EventBus eventBus) {
    return new SubscribeTo(eventBus);
  }

  public static EventBus getEventBus(final ProcessEngine processEngine) {
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

  /**
   * Ugly hack, delegate task shoul contain processdefinitionKey
   *
   * @param processDefinitionId
   * @return
   */
  public static String processDefintionKey(String processDefinitionId) {
    return processDefinitionId.replaceAll("(\\w+):\\d+:\\d+", "$1");
  }

  private CamundaReactor() {
    // util class
  }
}
