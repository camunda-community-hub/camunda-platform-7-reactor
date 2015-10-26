package org.camunda.bpm.extension.reactor.plugin;

import org.camunda.bpm.extension.reactor.listener.PublisherExecutionListener;
import org.camunda.bpm.extension.reactor.listener.PublisherTaskListener;
import reactor.bus.EventBus;

public class ReactorBpmnParseListener extends RegisterAllBpmnParseListener {

  public ReactorBpmnParseListener(final EventBus eventBus) {
    this(new PublisherTaskListener(eventBus), new PublisherExecutionListener(eventBus));
  }

  public ReactorBpmnParseListener(final PublisherTaskListener taskListener, final PublisherExecutionListener executionListener) {
    super(taskListener, executionListener);
  }

}
