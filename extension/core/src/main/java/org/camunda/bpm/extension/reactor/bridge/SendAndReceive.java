package org.camunda.bpm.extension.reactor.bridge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;

import reactor.bus.Event;
import reactor.fn.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * Abstraction for {@link reactor.bus.EventBus#sendAndReceive(Object, Event, Consumer)}.
 *
 * @param <T> type of input
 * @param <V> type of output
 */
interface SendAndReceive<T,V> extends GetEventType<T> {

  SynchronousEventBus eventBus();

  @Override
  Class<T> eventType();

  /**
   * Performs a sendAndReceive on a synchronous bus and collects all results.
   *
   * TODO: currently, this only collects the first result, no list is returned!
   *
   * @param input the command to send
   * @return list off all response events received. Empty when no one is subscribed.
   */
  default List<Event<V>> sendAndReceive(final T input) {
    requireNonNull(input);
    final List<Event<V>> result = Collections.synchronizedList(new ArrayList<Event<V>>());
    eventBus().sendAndReceive(
      eventType(),
      Event.wrap(input),
      (Consumer<Event<V>>) event -> result.add(event)
    );
    return result;
  }
}
