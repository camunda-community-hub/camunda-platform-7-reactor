package org.camunda.bpm.extension.reactor.bus;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.CamundaReactorTestHelper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import reactor.bus.Event;
import reactor.bus.selector.Selectors;
import reactor.fn.Consumer;

import static org.mockito.Mockito.verify;

@RunWith(Enclosed.class)
public class CamundaEventBusTest {

  public static abstract class Common {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public final MockitoRule mockito = MockitoJUnit.rule();

    protected final CamundaEventBus eventBus = new CamundaEventBus();

    @Mock
    protected ExecutionListener executionListener;

    @Mock
    protected TaskListener taskListener;
  }

  public static class PublishSubscribe extends Common {

    @Test
    public void notify_execution() throws Exception {
      eventBus.register(SelectorBuilder.selector(), executionListener);

      DelegateExecution execution = CamundaReactorTestHelper.delegateExecution();

      eventBus.notify(execution);

      verify(executionListener).notify(execution);
    }

    @Test
    public void notify_task() throws Exception {
      eventBus.register(SelectorBuilder.selector(), taskListener);

      DelegateTask task = CamundaReactorTestHelper.delegateTask();

      eventBus.notify(task);

      verify(taskListener).notify(task);
    }


  }


  public static class ErrorHandling extends Common {

    @Test
    public void raises_runtimeException_when_consumer_fails() throws Exception {
      eventBus.get().on(Selectors.matchAll(), new Consumer<Event<String>>() {
        @Override
        public void accept(Event<String> event) {
          throw new ProcessEngineException("fail");
        }
      });

      thrown.expect(RuntimeException.class);

      eventBus.get().notify(Selectors.$("any"), Event.wrap("event"));
    }

    @Test
    public void raises_bpmnError() throws Exception {
      eventBus.get().on(Selectors.matchAll(), new Consumer<Event<String>>() {
        @Override
        public void accept(Event<String> event) {
          throw new BpmnError("error");
        }
      });

      thrown.expect(BpmnError.class);

      eventBus.get().notify(Selectors.$("any"), Event.wrap("event"));

    }
  }

}
