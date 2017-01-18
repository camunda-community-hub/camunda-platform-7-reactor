package org.camunda.bpm.extension.reactor.bridge;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.Selectors;
import reactor.core.dispatch.SynchronousDispatcher;
import reactor.fn.Consumer;
import reactor.fn.Function;

public class SendAndReceiveSpike {

  private final EventBus eventBus = new EventBus(SynchronousDispatcher.INSTANCE);

  private final Logger logger = LoggerFactory.getLogger(SendAndReceiveSpike.class);

  @Test
  public void receiveTwice() throws Exception {
    eventBus.receive(Selectors.T(String.class), (Function<Event<String>, String>) stringEvent -> stringEvent.getData().toUpperCase());
    eventBus.receive(Selectors.T(String.class), (Function<Event<String>, String>) stringEvent -> stringEvent.getData().toLowerCase());

    eventBus.sendAndReceive(String.class, Event.wrap("helloWorld"), (Consumer<Event<String>>) stringEvent -> logger.info(stringEvent.toString()));
  }
}
