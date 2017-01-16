package org.camunda.bpm.extension.reactor.bridge;


import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;
import org.junit.Ignore;
import org.junit.Test;
import reactor.bus.Event;
import reactor.bus.selector.Selectors;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SendAndReceiveTest {

  private final SynchronousEventBus eventBus = new SynchronousEventBus();

  final SendAndReceive<String,String> bridge = new SendAndReceive<String, String>() {
    @Override
    public SynchronousEventBus eventBus() {
      return eventBus;
    }

    @Override
    public Class<String> eventType() {
      return String.class;
    }
  };

  @Test
  public void sendAndReceive_single_result() throws Exception {
    eventBus.receive(Selectors.T(String.class), (reactor.fn.Function<Event<String>, String>) stringEvent -> stringEvent.getData() + "-A");
    eventBus.receive(Selectors.T(String.class), (reactor.fn.Function<Event<String>, String>) stringEvent -> stringEvent.getData() + "-B");

    List<Event<String>> events = bridge.sendAndReceive("foo");

    assertThat(events).hasSize(1);
    assertThat(events.get(0).getData()).isEqualTo("foo-A");
  }

  @Test
  @Ignore
  public void sendAndReceive_collect_multiple() throws Exception {

    eventBus.receive(Selectors.T(String.class), (reactor.fn.Function<Event<String>, String>) stringEvent -> stringEvent.getData() + "-B");
    eventBus.receive(Selectors.T(String.class), (reactor.fn.Function<Event<String>, String>) stringEvent -> stringEvent.getData() + "-A");

    List<Event<String>> events = bridge.sendAndReceive("foo");

    assertThat(events).hasSize(2);
    assertThat(events.get(0).getData()).isEqualTo("foo-B");
    assertThat(events.get(1).getData()).isEqualTo("foo-A");
  }

}
