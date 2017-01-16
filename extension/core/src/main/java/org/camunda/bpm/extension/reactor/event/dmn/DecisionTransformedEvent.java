package org.camunda.bpm.extension.reactor.event.dmn;


import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.extension.reactor.util.PairEvent;
import org.camunda.bpm.model.dmn.instance.Decision;

public class DecisionTransformedEvent extends PairEvent<Decision, DmnDecision> {

  public DecisionTransformedEvent(final Decision left, final DmnDecision right) {
    super(left, right);
  }

  public Decision getDecision() {
    return getLeft();
  }

  public DmnDecision getDmnDecision() {
    return getRight();
  }
}
