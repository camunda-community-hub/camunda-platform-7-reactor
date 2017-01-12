package org.camunda.bpm.extension.reactor.bus;

import reactor.bus.EventBus;
import reactor.core.dispatch.SynchronousDispatcher;

/**
 * EventBus with preconfigured synchronous dispatcher and error handler that throws unchecked runtime
 * exception when processing register events fails.
 *
 * Due to the nature register camundas delegates (execution/task), no other dispatcher than synchronous can be used!
 */
public class SynchronousEventBus extends EventBus {

  public SynchronousEventBus() {
    super(SynchronousDispatcher.INSTANCE, null, null, UncaughtErrorHandler.INSTANCE);

  }
}
