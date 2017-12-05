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

package org.camunda.bpm.extension.reactor.projectreactor.routing;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Consumer;

import org.camunda.bpm.extension.reactor.projectreactor.Event;
import org.camunda.bpm.extension.reactor.projectreactor.registry.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jon Brisbin
 */
public class TraceableDelegatingRouter implements Router {

  private final Router delegate;
  private final Logger log;

  public TraceableDelegatingRouter(Router delegate) {
    requireNonNull(delegate, "Delegate EventRouter cannot be null.");
    this.delegate = delegate;
    this.log = LoggerFactory.getLogger(delegate.getClass());
  }

  @Override
  public <E extends Event<?>> void route(Object key,
                                         E event,
                                         List<Registration<Object, ? extends Consumer<? extends Event<?>>>> consumers,
                                         Consumer<E> completionConsumer,
                                         Consumer<Throwable> errorConsumer) {
    if (log.isTraceEnabled()) {
      log.trace("route({}, {}, {}, {}, {})", key, event, consumers, completionConsumer, errorConsumer);
    }
    delegate.route(key, event, consumers, completionConsumer, errorConsumer);
  }

}
