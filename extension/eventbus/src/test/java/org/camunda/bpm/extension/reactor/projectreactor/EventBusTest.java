package org.camunda.bpm.extension.reactor.projectreactor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Consumer;

import org.camunda.bpm.extension.reactor.projectreactor.dispatch.SynchronousDispatcher;
import org.camunda.bpm.extension.reactor.projectreactor.selector.Selectors;
import org.camunda.bpm.extension.reactor.projectreactor.spec.EventBusSpec;
import org.junit.Test;

public class EventBusTest {

  private static final Consumer<Throwable> UNCAUGHT = t -> {
    throw new RuntimeException(t);
  };

  private final EventBus eventBus = new EventBusSpec().dispatcher(SynchronousDispatcher.INSTANCE).uncaughtErrorHandler(UNCAUGHT).get();

  @Test
  public void notify_and_wrap() throws Exception {
    final AtomicConsumer<String> c = new AtomicConsumer<>();
    eventBus.on(Selectors.$("foo"), c);

    eventBus.notifyAndWrap("foo", "bar");

    assertThat(c.isReady()).isTrue();
    assertThat(c.getData().get()).isEqualTo("bar");
  }
}
