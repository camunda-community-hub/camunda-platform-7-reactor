package org.camunda.bpm.extension.reactor.plugin;

import org.camunda.bpm.extension.reactor.listener.PublisherCaseExecutionListener;
import org.camunda.bpm.extension.reactor.listener.PublisherTaskListener;
import reactor.bus.EventBus;

/**
 * @author Malte.Soerensen
 */
public class ReactorCmmnTransformListener extends RegisterAllCmmnTransformListener {

  public ReactorCmmnTransformListener(EventBus eventBus) {
    this(new PublisherTaskListener(eventBus), new PublisherCaseExecutionListener(eventBus));
  }

  public ReactorCmmnTransformListener(final PublisherTaskListener taskListener, final PublisherCaseExecutionListener executionListener) {
    super(taskListener, executionListener);
  }

}
