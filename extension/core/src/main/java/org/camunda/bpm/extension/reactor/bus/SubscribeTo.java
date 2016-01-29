package org.camunda.bpm.extension.reactor.bus;

import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.event.DelegateEventConsumer;
import org.camunda.bpm.extension.reactor.listener.SubscriberCaseExecutionListener;
import org.camunda.bpm.extension.reactor.listener.SubscriberExecutionListener;
import org.camunda.bpm.extension.reactor.listener.SubscriberTaskListener;
import reactor.bus.EventBus;
import reactor.bus.selector.Selector;

/**
 * Defining all possible type safe registrations via eventBus.on().
 */
public interface SubscribeTo {

  void on(Selector topic, SubscriberTaskListener listener);
  void on(SelectorBuilder topicBuilder, SubscriberTaskListener listener);

  void on(Selector topic, SubscriberExecutionListener listener);
  void on(SelectorBuilder topicBuilder, SubscriberExecutionListener listener);

  void on(Selector topic, SubscriberCaseExecutionListener listener);
  void on(SelectorBuilder topicBuilder, SubscriberCaseExecutionListener listener);

  void on(Selector topic, TaskListener listener);
  void on(SelectorBuilder topicBuilder, TaskListener listener);

  void on(Selector topic, ExecutionListener listener);
  void on(SelectorBuilder topicBuilder, ExecutionListener listener);

  void on(Selector topic, CaseExecutionListener listener);
  void on(SelectorBuilder topicBuilder, CaseExecutionListener listener);

  void on(SelectorBuilder topicBuilder, DelegateEventConsumer consumer);

}
