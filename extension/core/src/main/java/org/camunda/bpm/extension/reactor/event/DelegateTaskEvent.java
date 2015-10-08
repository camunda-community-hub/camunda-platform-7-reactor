package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.delegate.DelegateTask;
import reactor.bus.Event;

public class DelegateTaskEvent extends Event<DelegateTask> {

  public DelegateTaskEvent(DelegateTask data) {
    super(data);
    getId();
  }
}
