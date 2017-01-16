package org.camunda.bpm.extension.reactor.plugin.parse;


import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.dmn.DecisionsEvaluationBuilder;
import org.camunda.bpm.engine.repository.DecisionDefinition;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.extension.reactor.ReactorProcessEngineConfiguration;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.SynchronousEventBus;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
@Deployment(resources = "FooBarDiagram.dmn")
public class RegisterAllDmnTransformListenerTest {

  private final SynchronousEventBus eventBus = new SynchronousEventBus();
  private final CamundaEventBus camundaEventBus = new CamundaEventBus(eventBus);

  private final Logger logger = LoggerFactory.getLogger(RegisterAllDmnTransformListener.class);

  @Rule
  public final ProcessEngineRule processEngine = ReactorProcessEngineConfiguration.buildRule(camundaEventBus);

  @Test
  public void evaluateDmn() throws Exception {

    logger.info(processEngine.getRepositoryService().createDecisionRequirementsDefinitionQuery().list().toString());
    logger.info(processEngine.getRepositoryService().createDecisionDefinitionQuery().list().toString());


    DmnDecisionTableResult re = processEngine.getDecisionService().evaluateDecisionTableByKey("decision_a", Variables.putValue("name", "foo"));
String r =  re.getSingleEntry();
    assertThat(r).isEqualTo("Result");
  }
}
