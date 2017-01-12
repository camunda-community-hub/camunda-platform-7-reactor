package org.camunda.bpm.extension.reactor.bridge;


import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;
import org.junit.Test;

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
    SenderFunction.register(
      eventBus,
      command -> Optional.ofNullable(command.name).map(String::toUpperCase),
      Command.class
    );

    Function<Command, Optional<String>> function = ReceiverFunction.of(eventBus, Command.class);

    assertThat(function.apply(new Command("foo")).get()).isEqualTo("FOO");
  }
}
