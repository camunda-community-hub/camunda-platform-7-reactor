package org.camunda.bpm.extension.reactor.plugin.listener;


import org.camunda.bpm.extension.reactor.event.CamundaEvent;

interface OnDeploy {

  void onDeploy(CamundaEvent event);
}
