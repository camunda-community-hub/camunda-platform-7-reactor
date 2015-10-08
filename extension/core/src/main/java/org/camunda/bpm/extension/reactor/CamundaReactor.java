package org.camunda.bpm.extension.reactor;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.extension.reactor.event.DelegateExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import reactor.bus.selector.Selector;
import reactor.bus.selector.Selectors;

import java.text.MessageFormat;

public class CamundaReactor {

  private static final String KEY_PROCESS = "process";
  private static final String KEY_ELEMENT = "element";
  private static final String KEY_EVENT = "event";

  private static final String HEADER_PROCESS = "{" + KEY_PROCESS + "}";
  private static final String HEADER_ELEMENT = "{" + KEY_ELEMENT + "}";
  private static final String HEADER_EVENT = "{" + KEY_EVENT + "}";

  public static final String CAMUNDE_TOPIC_FORMAT = "/camunda/{0}/{1}/{2}";
  public static final String CAMUNDE_TOPIC = MessageFormat.format(CAMUNDE_TOPIC_FORMAT, HEADER_PROCESS, HEADER_ELEMENT, HEADER_EVENT);

  public static String topic(final String process, final String element, final String event) {
    return MessageFormat.format(CAMUNDE_TOPIC_FORMAT, nullsafeDefault(process, HEADER_PROCESS), nullsafeDefault(element, HEADER_ELEMENT), nullsafeDefault(event, HEADER_EVENT));
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
    return Selectors.uri(topic(process,element,event));
  }

  private static String nullsafeDefault(String key, String defaultValue) {
    return key != null && !"".equals(key) ? key : defaultValue;
  }

  /**
   * Ugly hack, delegate task shoul contain processdefinitionKey
   * @param processDefinitionId
   * @return
   */
  static String processDefintionKey(String processDefinitionId) {
    return processDefinitionId.replaceAll("(\\w+):\\d+:\\d+", "$1");
  }
}
