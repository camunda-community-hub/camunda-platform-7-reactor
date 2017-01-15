package org.camunda.bpm.extension.reactor.bridge;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;
import reactor.bus.Event;

public interface EventBridge<T, V> extends SendAndReceive<T, V>, Function<T, V> {

  /**
   * Creates a function that does a synchronous sendAndReceive on the given bus with the topic eventType.
   * It then returns the return value of the sender function that listens on the given topic.
   *
   * @param eventBus  a synchronous event bus. This only works with the sync dispatcher!
   * @param eventType the type of the input parameter of the function, used as a topic/selector.
   * @param <T>       input type of the function
   * @param <V>       output type of the function
   * @return function that uses sendAndReceive to access registered sender
   */
  static <T, V> EventBridge<T, V> on(final SynchronousEventBus eventBus, final Class<T> eventType) {
    return new EventBridge<T, V>() {
      @Override
      public SynchronousEventBus eventBus() {
        return eventBus;
      }

      @Override
      public Class<T> eventType() {
        return eventType;
      }
    };
  }


  @Override
  default V apply(final T input) {
    final List<Event<V>> responses = sendAndReceive(input);
    Optional<V> response = responses.stream().map(Event::getData).reduce((x, y) -> {
      throw new IllegalStateException("received more than on result!");
    });

    return response.orElse(null);
  }
}
