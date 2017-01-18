package org.camunda.bpm.extension.reactor.plugin.listener;

import org.camunda.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.engine.impl.cmmn.transformer.AbstractCmmnTransformListener;
import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;
import org.camunda.bpm.model.cmmn.impl.instance.CasePlanModel;


public class CmmnDeployedListener extends AbstractCmmnTransformListener {

  private final SynchronousEventBus eventBus;

  public CmmnDeployedListener(final SynchronousEventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void transformCasePlanModel(CasePlanModel casePlanModel, CmmnActivity activity) {
    super.transformCasePlanModel(casePlanModel, activity);
  }

}
