package org.camunda.bpm.extension.reactor.projectreactor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class AtomicConsumerTest {

  private final AtomicConsumer<String> consumer = new AtomicConsumer<>();

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
}
