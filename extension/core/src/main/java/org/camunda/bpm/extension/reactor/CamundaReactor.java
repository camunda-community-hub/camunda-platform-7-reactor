package org.camunda.bpm.extension.reactor;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.extension.reactor.event.DelegateExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import org.camunda.bpm.extension.reactor.listener.SubscriberExecutionListener;
import org.camunda.bpm.extension.reactor.listener.SubscriberTaskListener;
import reactor.bus.EventBus;
import reactor.bus.selector.Selector;
import reactor.bus.selector.Selectors;

import java.util.HashMap;
import java.util.Map;

public final class CamundaReactor {

  public static final String CAMUNDA_TOPIC = "/camunda/{process}/{element}/{event}";

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
  }

  public static String topic(final String process, final String element, final String event) {
    String topic = CAMUNDA_TOPIC;
    for (Map.Entry<String, String> entry : new HashMap<String, String>() {{
      put("{process}", process);
      put("{element}", element);
      put("{event}", event);
    }}.entrySet()) {
      if (entry.getValue() != null && !"".equals(entry.getKey())) {
        topic = topic.replace(entry.getKey(), entry.getValue());
      }
    }

    return topic;
  }

  public static String topic(final DelegateTask delegateTask) {
    return topic(processDefintionKey(delegateTask.getProcessDefinitionId()), delegateTask.getTaskDefinitionKey(), delegateTask.getEventName());
  }

  public static String topic(final DelegateExecution delegateExecution) {
    return topic(processDefintionKey(delegateExecution.getProcessDefinitionId()), delegateExecution.getCurrentActivityName(), delegateExecution.getEventName());
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

  /**
   * Ugly hack, delegate task shoul contain processdefinitionKey
   *
   * @param processDefinitionId
   * @return
   */
  static String processDefintionKey(String processDefinitionId) {
    return processDefinitionId.replaceAll("(\\w+):\\d+:\\d+", "$1");
  }

  private CamundaReactor() {
    // util class
  }
}
