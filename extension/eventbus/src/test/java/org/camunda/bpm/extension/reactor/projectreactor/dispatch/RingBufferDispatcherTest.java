package org.camunda.bpm.extension.reactor.projectreactor.dispatch;

import org.junit.Test;

public class RingBufferDispatcherTest {
  @Test
  public void testAwaitAndShutdownDoesNotHangForever() {
    for (int i = 0; i < 1000; i++) {
      RingBufferDispatcher dispatcher = new RingBufferDispatcher("dispatcher", 128);
      dispatcher.awaitAndShutdown();
    }
  }
}
