package org.camunda.bpm.extension.reactor.bridge;


import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.Selectors;

import java.util.function.Function;

public interface SenderFunction<T,V> extends Function<T,V>, GetEventType<T> {

  static <T,V> SenderFunction<T,V> register(EventBus eventBus, Function<T,V> function, Class<T> eventType) {
    SenderFunction<T,V> senderFunction = new SenderFunction<T, V>() {
      @Override
      public Class<T> eventType() {
        return eventType;
      }

      @Override
      public V apply(T t) {
        return function.apply(t);
      }
    };
    senderFunction.register(eventBus);
    return senderFunction;
  }

  default void register(final EventBus eventBus) {
    eventBus.receive(Selectors.T(eventType()), (reactor.fn.Function<Event<T>, V>) event -> SenderFunction.this.apply(event.getData()));
  }

  @Override
  Class<T> eventType();
}
