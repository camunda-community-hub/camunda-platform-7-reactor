package org.camunda.bpm.extension.reactor.bus;


import org.camunda.bpm.engine.ProcessEngineException;
import reactor.fn.Consumer;

/**
 * Singleton instance of errorHandler that applies when execution of listeners fails.
 */
public enum UncaughtErrorHandler implements Consumer<Throwable> {
  INSTANCE;

  @Override
  public void accept(final Throwable throwable) {
    if (throwable instanceof ProcessEngineException) {
      throw (ProcessEngineException) throwable;
    } else if (throwable instanceof RuntimeException) {
      throw (RuntimeException) throwable;
    }
    throw new RuntimeException(throwable);
  }
}
