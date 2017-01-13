package org.camunda.bpm.extension.reactor.bridge;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;

import reactor.bus.Event;
import reactor.fn.Consumer;

interface EventBridge<T,V> extends GetEventType<T> {

  SynchronousEventBus eventBus();

  @Override
  Class<T> eventType();

  /**
   * Performs a sendAndReceive on a synchronous bus and collects all results.
   * @param input the command to send
   * @return list off all response events received. Empty when no one is subscribed.
   */
  default List<Event<V>> sendAndReceive(final T input) {
    final List<Event<V>> result = new ArrayList<>();
    eventBus().sendAndReceive(
      eventType(),
      Event.wrap(input),
      (Consumer<Event<V>>) event -> result.add(event)
    );
    return result;
  }
}
