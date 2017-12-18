package org.camunda.bpm.extension.reactor.projectreactor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.extension.reactor.projectreactor.selector.Selectors.$;
import static org.camunda.bpm.extension.reactor.projectreactor.support.ErrorHandler.THROW_RUNTIME_EXCEPTION;

import java.util.function.Function;

import org.camunda.bpm.extension.reactor.projectreactor.dispatch.SynchronousDispatcher;
import org.camunda.bpm.extension.reactor.projectreactor.spec.EventBusSpec;
import org.junit.Test;

public class AtomicConsumerTest {

  private final AtomicConsumer<String> consumer = new AtomicConsumer<>();
  private final EventBus eventBus = new EventBusSpec()
    .dispatcher(SynchronousDispatcher.INSTANCE)
    .uncaughtErrorHandler(THROW_RUNTIME_EXCEPTION)
    .get();

  @Test
  public void empty_if_no_event_caught() throws Exception {
    assertThat(consumer.get()).isNull();
    assertThat(consumer.isReady()).isFalse();
    assertThat(consumer.getData().isPresent()).isFalse();
    assertThat(consumer.getHeaders().isPresent()).isFalse();
  }

  @Test
  public void filled_after_event_caught() throws Exception {
    final Event<String> event = Event.wrap("foo");
    event.getHeaders().set("hello", "world");

    consumer.accept(event);

    assertThat(consumer.isReady()).isTrue();
    assertThat(consumer.get()).isNotNull().isEqualTo(event);

    assertThat(consumer.getData().get()).isEqualTo("foo");
    assertThat(consumer.getHeaders().get().<String>get("hello")).isEqualTo("world");
  }

  @Test
  public void fill_with_sendAndReceive() throws Exception {
    final Function<Event<String>, String> upperCase = s -> s.getData().toUpperCase();

    eventBus.receive($("fill_with_sendAndReceive"), upperCase);
    eventBus.sendAndReceive("fill_with_sendAndReceive", Event.wrap("foo"), consumer);

    assertThat(consumer.isReady()).isTrue();
    assertThat(consumer.getData().get()).isEqualTo("FOO");
  }

}
