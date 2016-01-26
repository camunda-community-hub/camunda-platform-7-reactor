package org.camunda.bpm.extension.reactor;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.extension.reactor.listener.PublisherExecutionListener;
import org.camunda.bpm.extension.reactor.listener.SubscriberExecutionListener;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.Selectors;
import reactor.fn.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CamundaEventBusTest {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  private final EventBus eventBus = new CamundaEventBus();

  @Test
  public void raises_runtimeException_when_consumer_fails() throws Exception {
    eventBus.on(Selectors.matchAll(), new Consumer<Event<String>>() {
      @Override
      public void accept(Event<String> event) {
        throw new ProcessEngineException("fail");
      }
    });

    thrown.expect(RuntimeException.class);

    eventBus.notify(Selectors.$("any"), Event.wrap("event"));
  }

  @Test
  public void raises_bpmnError() throws Exception {
    eventBus.on(Selectors.matchAll(), new Consumer<Event<String>>() {
      @Override
      public void accept(Event<String> event) {
        throw new BpmnError("error");
      }
    });

    thrown.expect(BpmnError.class);

    eventBus.notify(Selectors.$("any"), Event.wrap("event"));

  }
}
