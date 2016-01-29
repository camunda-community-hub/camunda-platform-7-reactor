package org.camunda.bpm.extension.reactor.bus;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.extension.reactor.event.DelegateCaseExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.camunda.bpm.extension.reactor.event.DelegateEventConsumer;
import org.camunda.bpm.extension.reactor.event.DelegateExecutionEvent;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import org.camunda.bpm.extension.reactor.listener.SubscriberCaseExecutionListener;
import org.camunda.bpm.extension.reactor.listener.SubscriberExecutionListener;
import org.camunda.bpm.extension.reactor.listener.SubscriberTaskListener;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import reactor.bus.EventBus;
import reactor.bus.selector.Selector;
import reactor.core.dispatch.SynchronousDispatcher;
import reactor.fn.Consumer;

import java.util.List;

import static org.camunda.bpm.extension.reactor.bus.SelectorBuilder.selector;

/**
 * EventBus with camunda specific configuration.
 */
public class CamundaEventBus extends EventBus implements SubscribeTo {

  public static CamundaEventBus eventBus(final ProcessEngine processEngine) {
    ProcessEngineConfigurationImpl configuration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
    final List<ProcessEnginePlugin> plugins = configuration.getProcessEnginePlugins();

    if (plugins != null) {
      for (ProcessEnginePlugin plugin : plugins) {
        if (plugin instanceof ReactorProcessEnginePlugin) {
          return ((ReactorProcessEnginePlugin) plugin).getEventBus();
        }
      }
    }
    throw new IllegalStateException("No eventBus found. Make sure the Reactor plugin is configured correctly.");
  }

  public static CamundaEventBus eventBus() {
    final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    if (processEngine == null) {
      throw new IllegalStateException("No processEngine registered.");
    }
    return eventBus(processEngine);
  }

  private final TaskListener taskListener = new TaskListener() {
    @Override
    public void notify(DelegateTask delegateTask) {
      CamundaEventBus.this.notify(selector(delegateTask).key(), DelegateEvent.wrap(delegateTask));
    }
  };

  private final ExecutionListener executionListener = new ExecutionListener() {
    @Override
    public void notify(DelegateExecution execution) throws Exception {
      CamundaEventBus.this.notify(selector(execution).key(), DelegateEvent.wrap(execution));
    }

  };

  private final CaseExecutionListener caseExecutionListener = new CaseExecutionListener() {
    @Override
    public void notify(DelegateCaseExecution caseExecution) throws Exception {
      CamundaEventBus.this.notify(selector(caseExecution).key(), DelegateEvent.wrap(caseExecution));
    }
  };

  public CamundaEventBus() {
    super(SynchronousDispatcher.INSTANCE, null, null, UncaughtErrorHandler.INSTANCE);
  }

  @Override
  public void on(Selector topic, SubscriberTaskListener listener) {
    this.on(topic, (Consumer<DelegateTaskEvent>) listener);
  }

  @Override
  public void on(SelectorBuilder topicBuilder, SubscriberTaskListener listener) {
    on(topicBuilder.build(), listener);
  }

  @Override
  public void on(Selector topic, SubscriberExecutionListener listener) {
    this.on(topic, (Consumer<DelegateExecutionEvent>) listener);
  }

  @Override
  public void on(SelectorBuilder topicBuilder, SubscriberExecutionListener listener) {
    on(topicBuilder.build(), listener);
  }

  @Override
  public void on(Selector topic, SubscriberCaseExecutionListener listener) {
    this.on(topic, (Consumer<DelegateCaseExecutionEvent>) listener);
  }

  @Override
  public void on(SelectorBuilder topicBuilder, SubscriberCaseExecutionListener listener) {
    on(topicBuilder.build(), listener);
  }

  @Override
  public void on(Selector topic, TaskListener listener) {
    on(topic, SubscriberTaskListener.create(listener));
  }

  @Override
  public void on(SelectorBuilder topic, TaskListener listener) {
    on(topic.build(), SubscriberTaskListener.create(listener));
  }

  @Override
  public void on(Selector topic, ExecutionListener listener) {
    on(topic, SubscriberExecutionListener.create(listener));
  }

  @Override
  public void on(SelectorBuilder topicBuilder, ExecutionListener listener) {
    on(topicBuilder.build(), SubscriberExecutionListener.create(listener));
  }

  @Override
  public void on(Selector topic, CaseExecutionListener listener) {
    on(topic, SubscriberCaseExecutionListener.create(listener));
  }

  @Override
  public void on(SelectorBuilder topicBuilder, CaseExecutionListener listener) {
    on(topicBuilder.build(), SubscriberCaseExecutionListener.create(listener));
  }


  @Override
  public void on(SelectorBuilder topicBuilder, DelegateEventConsumer consumer) {
    on(topicBuilder.build(), consumer);
  }

  public SubscribeTo getListenerSubscriber() {
    return this;
  }

  /**
   * @return this eventbus downcasted to standard api
   */
  public EventBus get() {
    return this;
  }

  /**
   * @return caseExecutionListener that fires all parse events to bus
   */
  public CaseExecutionListener getCaseExecutionListener() {
    return caseExecutionListener;
  }

  /**
   * @return ExecutionListener that fires all parse events to bus
   */
  public ExecutionListener getExecutionListener() {
    return executionListener;
  }

  /**
   * @return taskListener that fires all parse events to bus
   */
  public TaskListener getTaskListener() {
    return taskListener;
  }
}
