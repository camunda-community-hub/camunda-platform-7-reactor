package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.extension.reactor.projectreactor.fn.Consumer;

public class DelegateExecutionEvent extends DelegateEvent<DelegateExecution> {

  public static Consumer<DelegateExecutionEvent> consumer(final ExecutionListener listener) {
    return new Consumer<DelegateExecutionEvent>() {
      @Override
      public void accept(DelegateExecutionEvent event) {
        try {
          listener.notify(event.getData());
        } catch (Exception e) {
          throw new ProcessEngineException(e);
        }
      }
    };
  }

  private static final long serialVersionUID = 1L;

  public DelegateExecutionEvent(final DelegateExecution data) {
    super(data);
  }
}
