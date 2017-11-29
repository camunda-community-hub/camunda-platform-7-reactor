/*
 * Copyright (c) 2011-2015 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.extension.reactor.projectreactor.core.processor;

import org.camunda.bpm.extension.reactor.projectreactor.core.Dispatcher;
import org.camunda.bpm.extension.reactor.projectreactor.core.support.NonBlocking;
import org.camunda.bpm.extension.reactor.projectreactor.fn.Resource;
import org.reactivestreams.Processor;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.function.Consumer;

/**
 * A base processor
 *
 * @author Stephane Maldini
 */
public abstract class ReactorProcessor<IN, OUT> implements
  Processor<IN, OUT>, Consumer<IN>, NonBlocking, Resource {

  //protected static final int DEFAULT_BUFFER_SIZE = 1024;

  protected static final int SMALL_BUFFER_SIZE = 32;

  protected final boolean autoCancel;

  @SuppressWarnings("unused")
  private volatile int subscriberCount = 0;
  protected static final AtomicIntegerFieldUpdater<ReactorProcessor> SUBSCRIBER_COUNT = AtomicIntegerFieldUpdater.newUpdater(ReactorProcessor.class, "subscriberCount");

  protected Subscription upstreamSubscription;

  public ReactorProcessor(boolean autoCancel) {
    this.autoCancel = autoCancel;
  }

  @Override
  public final void accept(IN e) {
    onNext(e);
  }

  @Override
  public void onSubscribe(final Subscription s) {
    if (this.upstreamSubscription != null) {
      s.cancel();
      return;
    }
    this.upstreamSubscription = s;
  }

  protected boolean incrementSubscribers() {
    return SUBSCRIBER_COUNT.getAndIncrement(this) == 0;
  }

  protected int decrementSubscribers() {
    Subscription subscription = upstreamSubscription;
    int subs = SUBSCRIBER_COUNT.decrementAndGet(this);
    if (subs == 0) {
      if (subscription != null && autoCancel) {
        upstreamSubscription = null;
        subscription.cancel();
      }
      return subs;
    }
    return subs;
  }

  public abstract long getAvailableCapacity();

  @Override
  public long getCapacity() {
    return Long.MAX_VALUE;
  }

  @Override
  public boolean isReactivePull(Dispatcher dispatcher, long producerCapacity) {
    return false;
  }


}
