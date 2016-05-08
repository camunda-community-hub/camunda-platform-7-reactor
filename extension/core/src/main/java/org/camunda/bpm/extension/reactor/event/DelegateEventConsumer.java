package org.camunda.bpm.extension.reactor.event;


import java.util.function.Consumer;

public abstract class DelegateEventConsumer implements Consumer<DelegateEvent> {

  @Override
  public abstract void accept(DelegateEvent delegateEvent);
}
