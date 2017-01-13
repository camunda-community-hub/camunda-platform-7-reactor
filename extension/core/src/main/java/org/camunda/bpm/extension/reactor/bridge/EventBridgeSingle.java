package org.camunda.bpm.extension.reactor.bridge;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;

import reactor.bus.Event;
import reactor.fn.Consumer;

public interface EventBridgeSingle<T, V> extends EventBridge<T,V>, Function<T, V> {

  /**
   * Creates a function that does a synchronous sendAndReceive on the given bus with the topic eventType.
   * It then returns the return value of the sender function that listens on the given topic.
   *
   * @param eventBus a synchronous event bus. This only works with the sync dispatcher!
   * @param eventType the type of the input parameter of the function, used as a topic/selector.
   * @param <T> input type of the function
   * @param <V> output type of the function
   * @return function that uses sendAndReceive to access registered sender
   */
  static <T,V> EventBridgeSingle<T,V> on(final SynchronousEventBus eventBus, final Class<T> eventType) {
    return new EventBridgeSingle<T, V>() {
      @Override
      public SynchronousEventBus  eventBus() {
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
   return null; //sendAndReceive(input).stream()
     //.map(Event::getData)
     //.collect(Collectors.toList());
  }
}
