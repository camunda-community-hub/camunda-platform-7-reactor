package org.camunda.bpm.extension.reactor.bridge;

public interface GetEventType<T> {

  Class<T> eventType();
}
