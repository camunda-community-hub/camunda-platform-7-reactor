package org.camunda.bpm.extension.reactor.listener;

import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.extension.reactor.event.DelegateCaseExecutionEvent;

public abstract class SubscriberCaseExecutionListener extends SubscriberListener<DelegateCaseExecutionEvent> implements CaseExecutionListener {

  public static SubscriberCaseExecutionListener create(final CaseExecutionListener caseExecutionListener) {
    return new SubscriberCaseExecutionListener() {
      @Override
      public void notify(DelegateCaseExecution delegateCaseExecution) throws Exception {
        caseExecutionListener.notify(delegateCaseExecution);
      }
    };
  }

  @Override
  public void accept(final DelegateCaseExecutionEvent delegateCaseExecutionEvent) {
    try {
      logger.debug("Received: {}", delegateCaseExecutionEvent);
      notify(delegateCaseExecutionEvent.getData());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
