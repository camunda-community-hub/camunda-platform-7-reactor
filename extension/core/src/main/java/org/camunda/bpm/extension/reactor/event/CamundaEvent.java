package org.camunda.bpm.extension.reactor.event;


import java.util.function.Consumer;

import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.registry.Registration;
import reactor.bus.selector.Selectors;

/**
 * @param <T> the type on the event value
 * @param <S> the type on the event itself, used for notify by type.
 */
public abstract class CamundaEvent <T, S extends CamundaEvent<T,S>> extends Event<T>  {

  protected static <T, S extends CamundaEvent<T, S>> Registration register(final EventBus eventBus, Class<S> eventType, final Consumer<S> consumer) {
    return eventBus.on(Selectors.T(eventType), (reactor.fn.Consumer<S>) event -> consumer.accept(event));
  }

  private final Class<S> type;

  protected CamundaEvent(final T data, final Class<S> type) {
    super(data);
    this.type = type;
    getId();
  }

  public Class<S> getType() {
    return type;
  }

  public void notify(final EventBus eventBus) {
    eventBus.notify(getType(), this);
  }
}
