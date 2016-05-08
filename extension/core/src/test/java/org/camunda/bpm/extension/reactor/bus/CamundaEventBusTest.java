package org.camunda.bpm.extension.reactor.bus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.extension.reactor.bus.SelectorBuilder.selector;
import static org.camunda.bpm.extension.reactor.bus.SelectorBuilder.Context.bpmn;
import static org.camunda.bpm.extension.reactor.bus.SelectorBuilder.Context.cmmn;
import static org.camunda.bpm.extension.reactor.bus.SelectorBuilder.Context.task;
import static org.mockito.Mockito.verify;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.CamundaReactorTestHelper;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder.Context;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import reactor.bus.Event;
import reactor.bus.selector.Selectors;

import java.util.function.Consumer;

@RunWith(Enclosed.class)
public class CamundaEventBusTest {

  public static abstract class Common {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public final MockitoRule mockito = MockitoJUnit.rule();

    protected final CamundaEventBus eventBus = new CamundaEventBus();

    @Mock
    protected DelegateExecution delegateExecution;

    @Mock
    protected DelegateCaseExecution delegateCaseExecution;

    protected DelegateTask delegateTask = CamundaReactorTestHelper.delegateTask();

    @Mock
    protected ExecutionListener executionListener;

    @Mock
    protected TaskListener taskListener;

    @Mock
    protected CaseExecutionListener caseExecutionListener;
  }

  public static class PreventWrongSubscription extends Common {

    @Test
    public void fails_to_register_caseExecutionListener_to_bpmn() throws Exception {
      thrown.expect(IllegalArgumentException.class);
      eventBus.register(selector().context(Context.bpmn), caseExecutionListener);
    }

    @Test
    public void fails_to_register_executionListener_to_cmmn() throws Exception {
      thrown.expect(IllegalArgumentException.class);
      eventBus.register(selector().context(Context.cmmn), executionListener);
    }

    @Test
    public void fails_to_register_taskListener_to_cmmn() throws Exception {
      thrown.expect(IllegalArgumentException.class);
      eventBus.register(selector().context(Context.cmmn), taskListener);
    }

    @Test
    public void fails_to_register_taskListener_to_bpmn() throws Exception {
      thrown.expect(IllegalArgumentException.class);
      eventBus.register(selector().context(Context.bpmn), taskListener);
    }

    @Test
    public void register_taskListener_to_task() throws Exception {
      eventBus.register(selector().context(Context.task), taskListener);
      // ok
    }

    @Test
    public void register_executionListener_to_bpmn() throws Exception {
      eventBus.register(selector().context(Context.bpmn), executionListener);
      // ok
    }

    @Test
    public void register_caseExecutionListener_to_cmmn() throws Exception {
      eventBus.register(selector().context(Context.cmmn), caseExecutionListener);
      // ok
    }
  }

  public static class PublishSubscribe extends Common {

    @CamundaSelector
    public class ToAnyTask implements TaskListener {

      private String value;

      @Override
      public void notify(DelegateTask delegateTask) {
        value = "foo";
      }}

    @Test
    public void tasklistener_registers_to_contextTask_automatically() throws Exception {
      ToAnyTask listener = new ToAnyTask();
      eventBus.register(listener);

      eventBus.notify(delegateTask);

      assertThat(listener.value).isEqualTo("foo");
    }

    @Test
    public void notify_execution() throws Exception {
      eventBus.register(SelectorBuilder.selector().context(bpmn), executionListener);

      DelegateExecution execution = CamundaReactorTestHelper.delegateExecution();

      eventBus.notify(execution);

      verify(executionListener).notify(execution);
    }

    @Test
    public void notify_task() throws Exception {
      eventBus.register(SelectorBuilder.selector().context(task), taskListener);

      DelegateTask task = CamundaReactorTestHelper.delegateTask();

      eventBus.notify(task);

      verify(taskListener).notify(task);
    }

    @Test
    public void notify_caseExecution() throws Exception {
      eventBus.register(SelectorBuilder.selector().context(cmmn), caseExecutionListener);

      DelegateCaseExecution execution= CamundaReactorTestHelper.delegateCaseExecution();

      eventBus.notify(execution);

      verify(caseExecutionListener).notify(execution);
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
