package org.camunda.bpm.extension.reactor.plugin.listener;


import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;
import org.camunda.bpm.extension.reactor.event.BpmnDeployedEvent;

public class BpmnDeployedListener extends AbstractBpmnParseListener {

  private final SynchronousEventBus eventBus;

  public BpmnDeployedListener(final SynchronousEventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void parseProcess(Element processElement, ProcessDefinitionEntity processDefinition) {
    BpmnDeployedEvent.of(processDefinition).notify(eventBus);
  }
}
