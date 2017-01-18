package org.camunda.bpm.extension.reactor.plugin.listener;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;
import org.camunda.bpm.extension.reactor.event.CamundaEvent;
import org.camunda.bpm.extension.reactor.event.DmnDeployedEvent;
import org.camunda.bpm.extension.reactor.util.AbstractDmnTransformListener;
import org.camunda.bpm.model.dmn.instance.Decision;

public class DmnDeployedListener extends AbstractDmnTransformListener implements OnDeploy{

  private final SynchronousEventBus eventBus;

  public DmnDeployedListener(final SynchronousEventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void transformDecision(final Decision decision, final DmnDecision dmnDecision) {
    onDeploy(DmnDeployedEvent.of(dmnDecision));
  }

  @Override
  public void onDeploy(CamundaEvent event) {
    event.notify(eventBus);
  }
}
