package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import reactor.fn.Consumer;

public class DelegateTaskEvent extends DelegateEvent<DelegateTask> {

  public static Consumer<DelegateTaskEvent> consumer(final TaskListener listener) {
    return event -> listener.notify(event.getData());
  }

  private static final long serialVersionUID = 1L;

  public DelegateTaskEvent(final DelegateTask data) {
    super(data);
  }
}
