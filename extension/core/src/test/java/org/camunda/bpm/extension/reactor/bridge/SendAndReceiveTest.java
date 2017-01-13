package org.camunda.bpm.extension.reactor.bridge;


import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class SendAndReceiveTest {

  private final SynchronousEventBus eventBus = new CamundaEventBus().get();

  public class Command {
    final String name;

    public Command(String name) {
      this.name = name;
    }

  }

  @Test
  public void sendAndReceive() throws Exception {
    EventBridgeSource.register(
      eventBus,
      command -> Optional.ofNullable(command.name).map(String::toUpperCase),
      Command.class
    );

    Function<Command, Collection<Optional<String>>> function = EventBridgeSingle.on(eventBus, Command.class);

    assertThat(function.apply(new Command("foo")).stream().findFirst().get()).isEqualTo("FOO");
  }

  @Test
  public void what_happens_with_multiple_senders() throws Exception {

    EventBridgeSource.register(
      eventBus,
      command -> Optional.ofNullable(command.name).map(String::toLowerCase),
      Command.class
    );
    EventBridgeSource.register(
      eventBus,
      command -> Optional.ofNullable(command.name).map(String::toUpperCase),
      Command.class
    );


    Function<Command, Collection<Optional<String>>> function = EventBridgeSingle.on(eventBus, Command.class);

    assertThat(function.apply(new Command("foo")).stream().findFirst().get()).isEqualTo("FOO");

  }

  @Test
  public void empty_list_when_no_supplier_is_registered() throws Exception {
    Function<Command, Collection<Optional<String>>> function = EventBridgeSingle.on(eventBus, Command.class);

    assertThat(function.apply(new Command("foo"))).isEmpty();
  }
}
