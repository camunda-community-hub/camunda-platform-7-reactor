package org.camunda.bpm.extension.reactor.bridge;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import reactor.bus.Event;
import reactor.fn.Consumer;


interface EventBridgeMulti<T,V> extends SendAndReceive<T,V>, Function<T, List<V>> {

  default List<Event<V>> sendAndReceive(final T input) {
    final List<Event<V>> result = new ArrayList<>();
    eventBus().sendAndReceive(
      eventType(),
      Event.wrap(input),
      (Consumer<Event<V>>) event -> result.add(event)
    );
    return result;
  }

  @Override
  default List<V> apply(final T input) {
    return sendAndReceive(input).stream()
      .map(Event::getData)
      .collect(Collectors.toList());
  }
}
