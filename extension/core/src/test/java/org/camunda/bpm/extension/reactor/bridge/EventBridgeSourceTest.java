package org.camunda.bpm.extension.reactor.bridge;

import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;
import org.junit.Test;
import reactor.bus.Event;
import reactor.fn.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class EventBridgeSourceTest {

  private final SynchronousEventBus eventBus = new SynchronousEventBus();

  @Test
  public void execute() throws Exception {
    EventBridgeSource<String, String> source = new EventBridgeSource<String, String>() {
      @Override
      public String apply(String s) {
        return s.toUpperCase();
      }

      @Override
      public Class<String> eventType() {
        return String.class;
      }
    };
    source.register(eventBus);

    StringBuffer b = new StringBuffer();
    eventBus.sendAndReceive(String.class, Event.wrap("foo"), new Consumer<Event<String>>() {
      @Override
      public void accept(Event<String> stringEvent) {
        b.append(stringEvent.getData());
      }
    });

    assertThat(b.toString()).isEqualTo("FOO");
  }
}
