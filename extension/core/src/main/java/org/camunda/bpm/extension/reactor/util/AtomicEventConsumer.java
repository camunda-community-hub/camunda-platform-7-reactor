package org.camunda.bpm.extension.reactor.util;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class AtomicEventConsumer<V> extends AtomicReference<V> implements Consumer<V> {

  @Override
  public void accept(V v) {
    set(v);
  }
}
