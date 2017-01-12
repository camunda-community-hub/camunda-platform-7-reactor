package org.camunda.bpm.extension.reactor.bus;

import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder.Context;
import org.camunda.bpm.extension.reactor.event.DelegateCaseExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.camunda.bpm.extension.reactor.event.DelegateEventConsumer;
import org.camunda.bpm.extension.reactor.event.DelegateExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import org.camunda.bpm.extension.reactor.event.PostInitEvent;
import org.camunda.bpm.extension.reactor.event.PostProcessEngineBuild;
import org.camunda.bpm.extension.reactor.event.PreInitEvent;
import org.camunda.bpm.extension.reactor.event.ProcessEnginePluginEvent;
import org.slf4j.Logger;
import reactor.bus.selector.Selectors;
import reactor.fn.Consumer;

import java.util.function.Supplier;

import static org.camunda.bpm.extension.reactor.bus.SelectorBuilder.selector;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Wrapper for reactor eventBus with camunda specific register and notify methods.
 */
public class CamundaEventBus implements Supplier<SynchronousEventBus> {

  private final Logger logger = getLogger(this.getClass());

  private final SynchronousEventBus eventBus;

  public CamundaEventBus() {
    this(new SynchronousEventBus());
  }

  public CamundaEventBus(SynchronousEventBus eventBus) {
    this.eventBus = eventBus;
  }

  private void notifyDelegateEvent(final String topic, final DelegateEvent event) {
    logger.debug("Notify execution: {} on topic {}", event.getData(), topic);
    eventBus.notify(topic, event);
  }

  public void notify(final DelegateCaseExecution caseExecution) {
    notifyDelegateEvent(selector(caseExecution).key(), DelegateEvent.wrap(caseExecution));
  }

  public void notify(final DelegateTask task) {
    notifyDelegateEvent(selector(task).key(), DelegateEvent.wrap(task));
  }

  public void notify(final DelegateExecution execution) {
    notifyDelegateEvent(selector(execution).key(), DelegateEvent.wrap(execution));
  }

  public void notify(final ProcessEnginePluginEvent processEnginePluginEvent) {
    eventBus.notify(processEnginePluginEvent.getType(), processEnginePluginEvent);
  }

  @Override
  public SynchronousEventBus get() {
    return eventBus;
  }

  /**
   * @return caseExecutionListener that fires all parse events to bus
   */
  public CaseExecutionListener getCaseExecutionListener() {
    return caseExecution -> CamundaEventBus.this.notify(caseExecution);
  }

  /**
   * @return ExecutionListener that fires all parse events to bus
   */
  public ExecutionListener getExecutionListener() {
    return execution -> CamundaEventBus.this.notify(execution);
  }

  /**
   * @return taskListener that fires all parse events to bus
   */
  public TaskListener getTaskListener() {
    return task -> CamundaEventBus.this.notify(task);
  }

  /**
   * Register listener for the topic parsed from CamundaSelector annotation.
   *
   * @see #register(SelectorBuilder, TaskListener)
   * @param listener the listener to register
   */
  public void register(final TaskListener listener) {
    register(SelectorBuilder.selector(listener), listener);
  }

  public void register(final ProcessEnginePlugin processEnginePlugin) {

    eventBus.on(Selectors.type(PreInitEvent.class), (Consumer<PreInitEvent>) event -> processEnginePlugin.preInit(event.getData()));

    eventBus.on(Selectors.type(PostInitEvent.class), (Consumer<PostInitEvent>) event -> processEnginePlugin.postInit(event.getData()));

    eventBus.on(Selectors.type(PostProcessEngineBuild.class), (Consumer<PostProcessEngineBuild>) event -> processEnginePlugin.postProcessEngineBuild(event.getData()));
  }

  /**
   * Register listener for the topic parsed from CamundaSelector annotation.
   *
   * @param topicBuilder the topic to register on
   * @param listener the listener to register
   */
  public void register(final SelectorBuilder topicBuilder, final TaskListener listener) {
    if (!Context.task.matches(topicBuilder)) {
      throw new IllegalArgumentException("can not register taskListener to topic: " + topicBuilder.key());
    }
    eventBus.on(topicBuilder.build(), DelegateTaskEvent.consumer(listener));
    logger.debug("registered {} to '{}'", listener.getClass().getSimpleName(), topicBuilder.key());
  }

  /**
   * Register listener for the topic parsed from CamundaSelector annotation.
   *
   * @see #register(SelectorBuilder, ExecutionListener)
   * @param listener the listener to register
   */
  public void register(final ExecutionListener listener) {
    register(SelectorBuilder.selector(listener), listener);
  }

  /**
   * Register listener for the topic parsed from CamundaSelector annotation.
   *
   * @param topicBuilder the topic to register on
   * @param listener the listener to register
   */
  public void register(final SelectorBuilder topicBuilder, final ExecutionListener listener) {
    if (!Context.bpmn.matches(topicBuilder)) {
      throw new IllegalArgumentException("can not register executionListener to topic: " + topicBuilder.key());
    }

    eventBus.on(topicBuilder.build(), DelegateExecutionEvent.consumer(listener));
    logger.debug("registered {} to '{}'", listener.getClass().getSimpleName(), topicBuilder.key());
  }

  /**
   * Register listener for the topic parsed from CamundaSelector annotation.
   *
   * @see #register(SelectorBuilder, CaseExecutionListener)
   * @param listener the listener to register
   */
  public void register(final CaseExecutionListener listener) {
    register(SelectorBuilder.selector(listener), listener);
  }

  /**
   * Register listener for the given topic.
   *
   * @param topicBuilder the topic to register on
   * @param listener the listener to register
   */
  public void register(final SelectorBuilder topicBuilder, final CaseExecutionListener listener) {
    if (!Context.cmmn.matches(topicBuilder)) {
      throw new IllegalArgumentException("can not register caseExecutionListener to topic: " + topicBuilder.key());
    }
    eventBus.on(topicBuilder.build(), DelegateCaseExecutionEvent.consumer(listener));
    logger.debug("registered {} to '{}'", listener.getClass().getSimpleName(), topicBuilder.key());
  }

  /**
   * Register generic consumer for given topic..
   *
   * @param topicBuilder the topic to register on
   * @param consumer the consumer to register
   */
  public void register(final SelectorBuilder topicBuilder, final DelegateEventConsumer consumer) {
    eventBus.on(topicBuilder.build(), consumer);
    logger.debug("registered {} to '{}'", consumer.getClass().getSimpleName(), topicBuilder.key());
  }

}
