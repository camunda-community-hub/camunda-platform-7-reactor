package org.camunda.bpm.extension.reactor;


import org.camunda.bpm.engine.delegate.BpmnModelExecutionContext;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.extension.reactor.listener.SubscriberExecutionListener;
import org.camunda.bpm.extension.reactor.listener.SubscriberListener;
import org.camunda.bpm.extension.reactor.listener.SubscriberTaskListener;

import reactor.bus.selector.Selector;
import reactor.bus.selector.Selectors;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.extension.reactor.CamundaReactor.processDefintionKey;

public class SelectorBuilder {

  public static SelectorBuilder selector() {
    return new SelectorBuilder();
  }

  public static SelectorBuilder selector(final DelegateTask delegateTask) {
    return selector()
      .type(extractTypeName(delegateTask))
      .process(processDefintionKey(delegateTask.getProcessDefinitionId()))
      .element(delegateTask.getTaskDefinitionKey())
      .event(delegateTask.getEventName());
  }

  public static SelectorBuilder selector(final DelegateExecution delegateExecution) {
    String typeName = extractTypeName(delegateExecution);
    String element = ("sequenceFlow".equals(typeName))
      ? delegateExecution.getCurrentTransitionId()
      : delegateExecution.getCurrentActivityId();

    return selector()
      .type(typeName)
      .process(processDefintionKey(delegateExecution.getProcessDefinitionId()))
      .element(element)
      .event(delegateExecution.getEventName());
  }

  public static SelectorBuilder selector(SubscriberTaskListener subscriberListener) {
    final CamundaSelector annotation = subscriberListener.getClass().getAnnotation(CamundaSelector.class);
    if (annotation == null) {
      throw new IllegalStateException(String.format("Unable to get @CamundaSelector annotation from %s.", subscriberListener.getClass().getName()));
    }
    return selector(annotation);
  }
  public static SelectorBuilder selector(SubscriberExecutionListener subscriberListenerType) {
    final CamundaSelector annotation = subscriberListenerType.getClass().getAnnotation(CamundaSelector.class);
    if (annotation == null) {
      throw new IllegalStateException(String.format("Unable to get @CamundaSelector annotation from %s.", subscriberListenerType.getClass().getName()));
    }
    return selector(annotation);
  }

  public static SelectorBuilder selector(final CamundaSelector annotation) {
    return selector()
      .type(annotation.type())
      .process(annotation.process())
      .element(annotation.element())
      .event(annotation.event());
  }

  private final Map<String, String> values = new HashMap<String, String>();

  private SelectorBuilder() {
    // noop
  }

  public SelectorBuilder process(String process) {
    values.put("{process}", process);

    return this;
  }

  public SelectorBuilder element(String element) {
    values.put("{element}", element);

    return this;
  }

  public SelectorBuilder event(String event) {
    values.put("{event}", event);

    return this;
  }

  public SelectorBuilder type(String type) {
    values.put("{type}", type);

    return this;
  }

  public Selector build() {
    return Selectors.uri(key());
  }

  public String key() {
    String topic = CamundaReactor.CAMUNDA_TOPIC;
    for (Map.Entry<String, String> entry : values.entrySet()) {
      if (entry.getValue() != null && !"".equals(entry.getValue()) && !"".equals(entry.getKey())) {
        topic = topic.replace(entry.getKey(), entry.getValue());
      }
    }
    return topic;
  }

  @Override
  public String toString() {
    return values.toString();
  }

  static String extractTypeName(BpmnModelExecutionContext bpmnModelExecutionContext) {
    return bpmnModelExecutionContext.getBpmnModelElementInstance().getElementType().getTypeName();
  }
}
