package org.camunda.bpm.extension.reactor.bridge;

interface GetEventType<T> {

  Class<T> eventType();
}
