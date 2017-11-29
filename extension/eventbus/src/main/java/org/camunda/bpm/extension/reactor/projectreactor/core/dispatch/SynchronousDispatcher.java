/*
 * Copyright (c) 2011-2014 Pivotal Software, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.camunda.bpm.extension.reactor.projectreactor.core.dispatch;

import org.camunda.bpm.extension.reactor.projectreactor.Environment;
import org.camunda.bpm.extension.reactor.projectreactor.core.Dispatcher;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A {@link org.camunda.bpm.extension.reactor.projectreactor.core.Dispatcher} implementation that dispatches events using the calling thread.
 *
 * @author Jon Brisbin
 * @author Stephane Maldini
 */
public final class SynchronousDispatcher implements Dispatcher {

  public static final SynchronousDispatcher INSTANCE = new SynchronousDispatcher();

  public SynchronousDispatcher() {
  }

  @Override
  public boolean alive() {
    return true;
  }

  @Override
  public boolean awaitAndShutdown() {
    return true;
  }

  @Override
  public boolean awaitAndShutdown(long timeout, TimeUnit timeUnit) {
    return true;
  }

  @Override
  public void shutdown() {
  }

  @Override
  public void forceShutdown() {
  }

  @Override
  public <E> void tryDispatch(E event,
                              Consumer<E> consumer,
                              Consumer<Throwable> errorConsumer) {
    dispatch(event, consumer, errorConsumer);
  }

  @Override
  public <E> void dispatch(E event,
                           Consumer<E> eventConsumer,
                           Consumer<Throwable> errorConsumer) {
    try {
      eventConsumer.accept(event);
    } catch (Exception e) {
      if (errorConsumer != null) {
        errorConsumer.accept(e);
      } else if (Environment.alive()) {
        Environment.get().routeError(e);
      }
    }
  }

  @Override
  public String toString() {
    return "immediate";
  }

  @Override
  public void execute(Runnable command) {
    command.run();
  }

  @Override
  public long remainingSlots() {
    return Long.MAX_VALUE;
  }

  @Override
  public boolean supportsOrdering() {
    return true;
  }

  @Override
  public long backlogSize() {
    return Long.MAX_VALUE;
  }

  @Override
  public boolean inContext() {
    return true;
  }
}
