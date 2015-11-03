package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.delegate.DelegateTask;

public class DelegateTaskEvent extends DelegateEvent<DelegateTask> {

  private static final long serialVersionUID = 1L;

  public DelegateTaskEvent(final DelegateTask data) {
    super(data);
  }
}
