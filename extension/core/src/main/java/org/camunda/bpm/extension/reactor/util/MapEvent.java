package org.camunda.bpm.extension.reactor.util;

import java.util.Map;

import reactor.bus.Event;

public class MapEvent extends Event<Map<String, Object>> {

  public MapEvent(final Map<String, Object> data) {
    super(data);
  }
}
