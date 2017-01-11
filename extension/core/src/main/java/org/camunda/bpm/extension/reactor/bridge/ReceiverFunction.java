package org.camunda.bpm.extension.reactor.bridge;

import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public interface ReceiverFunction<T, V> extends Function<T, V>, GetEventType<T> {

  static <T,V> ReceiverFunction<T,V> of(EventBus eventBus, Class<T> eventType) {
    return new ReceiverFunction<T, V>() {
      @Override
      public EventBus eventBus() {
        return eventBus;
      }

      @Override
      public Class<T> eventType() {
        return eventType;
      }
    };
  }

  EventBus eventBus();

  @Override
  Class<T> eventType();

  @Override
  default V apply(final T input) {
    final AtomicReference<V> result = new AtomicReference<V>();
    eventBus().sendAndReceive(
      eventType(),
      Event.wrap(input),
      (Consumer<Event<V>>) event -> result.set(event.getData())
    );
    return result.get();
  }
}
