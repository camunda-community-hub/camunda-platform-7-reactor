package org.camunda.bpm.extension.reactor.bridge;


import reactor.bus.Event;
import reactor.bus.selector.Selectors;

import java.util.function.Function;

import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;

public interface EventBridgeSource<T,V> extends Function<T,V>, GetEventType<T> {

  static <T,V> EventBridgeSource<T,V> register(SynchronousEventBus eventBus, Function<T,V> function, Class<T> eventType) {
    EventBridgeSource<T,V> wrapper = new EventBridgeSource<T, V>() {
      @Override
      public Class<T> eventType() {
        return eventType;
      }

      @Override
      public V apply(T t) {
        return function.apply(t);
      }
    };
    wrapper.register(eventBus);
    return wrapper;
  }

  default void register(final SynchronousEventBus eventBus) {
    eventBus.receive(Selectors.T(eventType()), (reactor.fn.Function<Event<T>, V>) event -> EventBridgeSource.this.apply(event.getData()));
  }

  @Override
  Class<T> eventType();
}
