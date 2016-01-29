package org.camunda.bpm.extension.reactor;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.*;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.camunda.bpm.extension.reactor.event.DelegateCaseExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import org.camunda.bpm.extension.reactor.listener.SubscriberExecutionListener;
import org.camunda.bpm.extension.reactor.listener.SubscriberTaskListener;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import reactor.bus.EventBus;
import reactor.bus.selector.Selector;

public final class CamundaReactor {

  public static final String CAMUNDA_TOPIC = "/camunda/{type}/{process}/{element}/{event}";

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

  public static SelectorBuilder selector(final DelegateTask delegateTask) {
    return SelectorBuilder.selector(delegateTask);
  }

  public static SelectorBuilder selector(final DelegateExecution delegateExecution) {
    return SelectorBuilder.selector(delegateExecution);
  }

  public static SelectorBuilder selector(final DelegateCaseExecution delegateCaseExecution) {
    return SelectorBuilder.selector(delegateCaseExecution);
  }

  public static SelectorBuilder selector(SubscriberTaskListener subscriberListener) {
    return SelectorBuilder.selector(subscriberListener);
  }

  public static SelectorBuilder selector(SubscriberExecutionListener subscriberListenerType) {
    return SelectorBuilder.selector(subscriberListenerType);
  }

  public static SelectorBuilder selector(final CamundaSelector annotation) {
    return SelectorBuilder.selector(annotation);
  }


  /**
   * Gets EventBus from given process engine via plugin.
   *
   * @see CamundaEventBus#eventBus(ProcessEngine)
   * @param processEngine the process engine
   * @return the camunda eventBus
   */
  public static CamundaEventBus eventBus(final ProcessEngine processEngine) {
    return CamundaEventBus.eventBus(processEngine);
  }

  /**
   * Gets EventBus from registered default process engine.
   *
   * @see CamundaEventBus#eventBus()
   * @return the camunda eventBus
   */
  public static CamundaEventBus eventBus() {
    return CamundaEventBus.eventBus();
  }


  public static ReactorProcessEnginePlugin plugin() {
    return new ReactorProcessEnginePlugin();
  }

  private CamundaReactor() {
    // util class
  }
}
