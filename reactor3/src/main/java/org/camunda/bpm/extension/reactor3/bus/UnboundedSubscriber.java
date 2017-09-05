package org.camunda.bpm.extension.reactor3.bus;


import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

import java.util.function.Consumer;

public abstract class UnboundedSubscriber<T> extends BaseSubscriber<T> {


  @Override
  protected void hookOnSubscribe(Subscription _unused) {
    requestUnbounded();
  }

}
