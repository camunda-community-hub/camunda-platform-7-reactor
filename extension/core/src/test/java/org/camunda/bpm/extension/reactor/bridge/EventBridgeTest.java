package org.camunda.bpm.extension.reactor.bridge;


import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;
import org.junit.Test;
import reactor.bus.Event;
import reactor.bus.selector.Selectors;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class EventBridgeTest {

  private final SynchronousEventBus eventBus = new SynchronousEventBus();

  private final Function<String,String> bridge = new EventBridge<String, String>() {
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
  public void single_result() throws Exception {
    eventBus.receive(
      Selectors.T(String.class),
      (reactor.fn.Function<Event<String>, String>) stringEvent -> stringEvent.getData().toUpperCase()
    );

    assertThat(bridge.apply("foo")).isEqualTo("FOO");
  }

  @Test
  public void no_source() throws Exception {
    assertThat(bridge.apply("foo")).isNull();
  }
}
