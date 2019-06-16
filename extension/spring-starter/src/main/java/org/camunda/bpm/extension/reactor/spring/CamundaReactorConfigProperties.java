package org.camunda.bpm.extension.reactor.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="camunda.bpm.reactor")
public class CamundaReactorConfigProperties {

  private boolean reactorListenerFirstOnUserTask = false;

  public boolean getReactorListenerFirstOnUserTask() {
    return this.reactorListenerFirstOnUserTask;
  }

  public void setReactorListenerFirstOnUserTask(final boolean val) {
    this.reactorListenerFirstOnUserTask = val;
  }
}
