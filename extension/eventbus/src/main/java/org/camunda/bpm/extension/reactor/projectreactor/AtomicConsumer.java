package org.camunda.bpm.extension.reactor.projectreactor;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * An {@link EventBus} {@link Consumer} that stores the callback value
 * from {@link EventBus#sendAndReceive(Object, Event, Consumer)} in an {@link AtomicReference}
 *
 * @param <V> type of return.
 */
public class AtomicConsumer<V> extends AtomicReference<Event<V>> implements Consumer<Event<V>> {

  @Override
  public void accept(final Event<V> event) {
    set(event);
  }

  public Optional<Event<V>> toOptional() {
    return Optional.ofNullable(get());
  }

  public Optional<Event.Headers> getHeaders() {
    return toOptional().map(Event::getHeaders);
  }

  public boolean isReady() {
    return toOptional().isPresent();
  }

  public Optional<V> getData() {
    return toOptional().map(Event::getData);
  }

}
