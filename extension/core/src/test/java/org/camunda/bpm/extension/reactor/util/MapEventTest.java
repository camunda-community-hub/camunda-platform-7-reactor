package org.camunda.bpm.extension.reactor.util;


import org.junit.Test;

public class MapEventTest {

  public static class FooBarEvent extends MapEvent {

    public FooBarEvent(String foo, String bar) {
      super(null);
    }
  }

  @Test
  public void sendReceive_mapEvent() throws Exception {

  }
}
