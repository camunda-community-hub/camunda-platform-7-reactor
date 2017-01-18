package org.camunda.bpm.extension.reactor.event;


import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.extension.reactor.model.ProcessDefinitionDto;

public class BpmnDeployedEvent extends CamundaEvent<ProcessDefinitionDto, BpmnDeployedEvent> {

  public static BpmnDeployedEvent of(final ProcessDefinition processDefinition) {
    return new BpmnDeployedEvent(ProcessDefinitionDto.from(processDefinition));
  }

  protected BpmnDeployedEvent(ProcessDefinitionDto data) {
    super(data, BpmnDeployedEvent.class);
  }
}
