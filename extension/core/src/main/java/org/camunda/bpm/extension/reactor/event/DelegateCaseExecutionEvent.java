package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import reactor.fn.Consumer;

public class DelegateCaseExecutionEvent extends DelegateEvent<DelegateCaseExecution> {

  public static Consumer<DelegateCaseExecutionEvent> consumer(final CaseExecutionListener listener) {
    return new Consumer<DelegateCaseExecutionEvent>() {
      @Override
      public void accept(DelegateCaseExecutionEvent event) {
        try {
          listener.notify(event.getData());
        } catch (Exception e) {
          throw new ProcessEngineException(e);
        }
      }
    };
  }


  private static final long serialVersionUID = 1L;

  public DelegateCaseExecutionEvent(final DelegateCaseExecution data) {
    super(data);
  }
}
