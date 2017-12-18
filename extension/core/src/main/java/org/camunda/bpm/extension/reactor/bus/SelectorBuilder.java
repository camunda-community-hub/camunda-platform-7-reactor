package org.camunda.bpm.extension.reactor.bus;


import static org.camunda.bpm.extension.reactor.bus.SelectorBuilder.Context.task;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.delegate.BpmnModelExecutionContext;
import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.CmmnModelExecutionContext;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.fn.GetProcessDefinitionKey;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;

import org.camunda.bpm.extension.reactor.projectreactor.selector.Selector;
import org.camunda.bpm.extension.reactor.projectreactor.selector.Selectors;

public class SelectorBuilder {

  public enum Context {
    task,
    bpmn,
    cmmn;

    public boolean matches(SelectorBuilder selectorBuilder) {
      return this.name().equals(selectorBuilder.values.get("{context}"));
    }
  }

  public static SelectorBuilder selector() {
    return new SelectorBuilder();
  }

  public static SelectorBuilder selector(final DelegateTask delegateTask) {
    return selector()
      .context(Context.task)
      .type(null)
      .process(GetProcessDefinitionKey.from(delegateTask))
      .element(delegateTask.getTaskDefinitionKey())
      .event(delegateTask.getEventName());
  }

  public static SelectorBuilder selector(final DelegateExecution delegateExecution) {
    String typeName = extractTypeName(delegateExecution);
    String element = ("sequenceFlow".equals(typeName))
      ? delegateExecution.getCurrentTransitionId()
      : delegateExecution.getCurrentActivityId();

    return selector()
      .context(Context.bpmn)
      .type(typeName)
      .process(GetProcessDefinitionKey.from(delegateExecution))
      .element(element)
      .event(delegateExecution.getEventName());
  }

  public static SelectorBuilder selector(final DelegateCaseExecution delegateCaseExecution) {
    String typeName = extractTypeName(delegateCaseExecution);
    String element = delegateCaseExecution.getActivityId();

    return selector()
      .context(Context.cmmn)
      .type(typeName)
      .caseDefinitionKey(GetProcessDefinitionKey.from(delegateCaseExecution))
      .element(element)
      .event(delegateCaseExecution.getEventName());
  }

  public static SelectorBuilder selector(final TaskListener listener) {
    return fromCamundaSelector(listener.getClass()).context(task).type(null);
  }

  public static SelectorBuilder selector(final ExecutionListener listener) {
    return fromCamundaSelector(listener.getClass()).context(Context.bpmn);
  }

  public static SelectorBuilder selector(final CaseExecutionListener listener) {
    return fromCamundaSelector(listener.getClass()).context(Context.cmmn);
  }

  static SelectorBuilder fromCamundaSelector(Class<?> annotatedClass) {
    CamundaSelector annotation = null;
    while (annotatedClass != Object.class) {
      annotation = annotatedClass.getAnnotation(CamundaSelector.class);
      if (annotation != null) {
        break;
      }
      annotatedClass = annotatedClass.getSuperclass();
    }

    if (annotation == null) {
      throw new IllegalStateException(String.format("Unable to get @CamundaSelector annotation from %s hierarchy.", annotatedClass.getName()));
    }
    return selector(annotation);
  }

  public static SelectorBuilder selector(final CamundaSelector annotation) {
    return selector()
      .context(annotation.context())
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

  public SelectorBuilder context(Context context) {
    values.put("{context}", context.name());

    return this;
  }

  public SelectorBuilder task() {
    return context(Context.task);
  }

  public SelectorBuilder cmmn() {
    return context(Context.cmmn);
  }


  public SelectorBuilder bpmn() {
    return context(Context.bpmn);
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
    FlowElement bpmnModelElementInstance = bpmnModelExecutionContext.getBpmnModelElementInstance();
    return bpmnModelElementInstance.getElementType().getTypeName();
  }

  static String extractTypeName(CmmnModelExecutionContext cmmnModelExecutionContext) {
    CmmnElement cmmnModelElementInstance = cmmnModelExecutionContext.getCmmnModelElementInstance();
    return cmmnModelElementInstance.getElementType().getTypeName();
  }
}
