package org.camunda.bpm.extension.reactor;

import org.camunda.bpm.engine.ProcessEngineException;
import reactor.bus.EventBus;
import reactor.core.dispatch.SynchronousDispatcher;
import reactor.fn.Consumer;

/**
 * EventBus with camunda specific configuration.
 */
public class CamundaEventBus extends EventBus {

  static final Consumer<Throwable> ERROR_CONSUMER = new Consumer<Throwable> () {
    @Override
    public void accept(Throwable throwable) {
      if (throwable instanceof ProcessEngineException) {
        throw (ProcessEngineException)throwable;
      } else if (throwable instanceof RuntimeException) {
        throw (RuntimeException) throwable;
      }
      throw new RuntimeException(throwable);
    }
  };

  public CamundaEventBus() {
    super(SynchronousDispatcher.INSTANCE, null, null, ERROR_CONSUMER);
  }

}
