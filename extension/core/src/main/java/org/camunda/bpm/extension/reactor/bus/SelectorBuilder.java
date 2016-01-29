package org.camunda.bpm.extension.reactor.bus;


import org.camunda.bpm.engine.delegate.BpmnModelExecutionContext;
import org.camunda.bpm.engine.delegate.CmmnModelExecutionContext;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.listener.SubscriberCaseExecutionListener;
import org.camunda.bpm.extension.reactor.listener.SubscriberExecutionListener;
import org.camunda.bpm.extension.reactor.listener.SubscriberTaskListener;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;
import reactor.bus.selector.Selector;
import reactor.bus.selector.Selectors;

import java.util.HashMap;
import java.util.Map;

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

  public static SelectorBuilder selector(final DelegateCaseExecution delegateCaseExecution) {
    String typeName = extractTypeName(delegateCaseExecution);
    String element = delegateCaseExecution.getActivityId();

    return selector()
      .type(typeName)
      .caseDefinitionKey(caseDefintionKey(delegateCaseExecution.getCaseDefinitionId()))
      .element(element)
      .event(delegateCaseExecution.getEventName());
  }

  public static SelectorBuilder selector(SubscriberTaskListener subscriberListener) {
    return fromCamundaSelector(subscriberListener.getClass());
  }

  public static SelectorBuilder selector(SubscriberExecutionListener subscriberListenerType) {
    return fromCamundaSelector(subscriberListenerType.getClass());
  }

  public static SelectorBuilder selector(SubscriberCaseExecutionListener subscriberListenerType) {
    return fromCamundaSelector(subscriberListenerType.getClass());
  }

  static SelectorBuilder fromCamundaSelector(Class<?> annotatedClass) {
    final CamundaSelector annotation = annotatedClass.getAnnotation(CamundaSelector.class);
    if (annotation == null) {
      throw new IllegalStateException(String.format("Unable to get @CamundaSelector annotation from %s.", annotatedClass.getName()));
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

  public SelectorBuilder caseDefinitionKey(String caseDefinitionKey) {
    //the caseDefinitionKey has to be put into the 'process' variable,
    //because otherwise the topic template string in CamundaReactor.CAMUNDA_TOPIC won't work
    return process(caseDefinitionKey);
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

  /**
   * Ugly hack, delegate task should contain processdefinitionKey.
   *
   * @param processDefinitionId the process definition id
   * @return process definition key
   */
  public static String processDefintionKey(String processDefinitionId) {
    return processDefinitionId.replaceAll("(\\w+):\\d+:\\d+", "$1");
  }

  static String processDefintionKey(DelegateExecution execution) {
    return processDefintionKey(execution.getProcessDefinitionId());
  }

  static String processDefintionKey(DelegateTask task) {
    return processDefintionKey(task.getProcessDefinitionId());
  }

  /**
   * Yet anpother ugly hack, delegate task should contain caseDefinitionKey.
   *
   * @param caseDefinitionId
   * @return case definition key
   * @see #processDefintionKey(String)
   */
  public static String caseDefintionKey(String caseDefinitionId) {
    return processDefintionKey(caseDefinitionId);
  }

  static String caseDefintionKey(DelegateCaseExecution execution) {
    return caseDefintionKey(execution.getCaseDefinitionId());
  }

  @Override
  public String toString() {
    return values.toString();
  }

  static String extractTypeName(BpmnModelExecutionContext bpmnModelExecutionContext) {
    FlowElement bpmnModelElementInstance = bpmnModelExecutionContext.getBpmnModelElementInstance();
    return bpmnModelElementInstance.getElementType().getTypeName();
  }

  static String extractTypeName(CmmnModelExecutionContext cmmnModelExecutionContext) {
    CmmnElement cmmnModelElementInstance = cmmnModelExecutionContext.getCmmnModelElementInstance();
    return cmmnModelElementInstance.getElementType().getTypeName();
  }
}
