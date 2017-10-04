package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.extension.reactor.projectreactor.fn.Consumer;

public abstract class DelegateEventConsumer implements Consumer<DelegateEvent> {

  @Override
  public abstract void accept(DelegateEvent delegateEvent);
}
