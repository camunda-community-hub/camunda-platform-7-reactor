package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.delegate.DelegateTask;
import reactor.bus.Event;

public class DelegateTaskEvent extends DelegateEvent<DelegateTask> {

  public DelegateTaskEvent(final DelegateTask data) {
    super(data);
  }
}
