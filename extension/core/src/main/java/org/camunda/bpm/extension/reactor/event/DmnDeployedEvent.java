package org.camunda.bpm.extension.reactor.event;


import java.util.function.Consumer;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;
import org.camunda.bpm.extension.reactor.model.DecisionDto;

import reactor.bus.EventBus;
import reactor.bus.registry.Registration;

public class DmnDeployedEvent extends CamundaEvent<DecisionDto, DmnDeployedEvent> {

  public static DmnDeployedEvent of(final DmnDecision decision) {
    return new DmnDeployedEvent(DecisionDto.from(decision));
  }

  public static Registration register(final SynchronousEventBus eventBus, final Consumer<DmnDeployedEvent> consumer) {
    return CamundaEvent.register(eventBus, DmnDeployedEvent.class, consumer);
  }

  protected DmnDeployedEvent(DecisionDto data) {
    super(data, DmnDeployedEvent.class);
  }
}
