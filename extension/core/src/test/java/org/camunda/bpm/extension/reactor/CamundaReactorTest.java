package org.camunda.bpm.extension.reactor;

import static org.assertj.core.api.Assertions.assertThat;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class CamundaReactorTest {

  static {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
  }

  @Rule
  public final ExpectedException thrown = ExpectedException.none();


  @Test
  public void camunda_topic() {
    assertThat(CamundaReactor.CAMUNDA_TOPIC).isEqualTo("/camunda/{type}/{process}/{element}/{event}");
  }

  @Test
  public void creates_topic_for_process_element_and_event() {
    assertThat(CamundaReactor.selector().process("process").element("task").event("create").key()).isEqualTo("/camunda/{type}/process/task/create");
  }

  @Test
  public void creates_general_topic_for_null_values() {
    assertThat(CamundaReactor.selector().key()).isEqualTo("/camunda/{type}/{process}/{element}/{event}");
  }

  @Test
  public void creates_topic_for_element() {
    assertThat(CamundaReactor.selector().element("task").key()).isEqualTo("/camunda/{type}/{process}/task/{event}");
  }

  @Test
  public void creates_topic_for_process() {
    assertThat(CamundaReactor.selector().process("foo").key()).isEqualTo("/camunda/{type}/foo/{element}/{event}");
  }

  @Test
  public void creates_topic_for_event() {
    assertThat(CamundaReactor.selector().event("bar").key()).isEqualTo("/camunda/{type}/{process}/{element}/bar");
  }

  @Test
  public void retrieve_processDefinitionKey_from_definitionId() {
    assertThat(SelectorBuilder.processDefintionKey("process_a:1:3")).isEqualTo("process_a");
  }

  @Test
  public void retrieve_caseDefinitionKey_from_definitionId() {
    assertThat(SelectorBuilder.caseDefintionKey("case_a:1:3")).isEqualTo("case_a");
  }

  @Test
  public void get_eventBus_from_engine() {
    ProcessEngine engine = new ReactorProcessEngineConfiguration().buildProcessEngine();

    try {
      assertThat(CamundaReactor.eventBus(engine)).isEqualTo(ReactorProcessEnginePlugin.CAMUNDA_EVENTBUS);

    } finally {
      engine.close();
    }
  }

  @Test
  public void get_eventBus_from_default_engine() {
    ProcessEngine engine = new ReactorProcessEngineConfiguration().buildProcessEngine();

    try {
      assertThat(CamundaReactor.eventBus()).isEqualTo(ReactorProcessEnginePlugin.CAMUNDA_EVENTBUS);
    } finally {
      engine.close();
    }
  }


  @Test
  public void fails_to_get_eventBus_from_engine() {
    ProcessEngine engine = new StandaloneInMemProcessEngineConfiguration(){{
      setDatabaseSchemaUpdate(ProcessEngineConfigurationImpl.DB_SCHEMA_UPDATE_DROP_CREATE);
    }}.buildProcessEngine();
    try {
      thrown.expect(IllegalStateException.class);
      thrown.expectMessage("No eventBus found. Make sure the Reactor plugin is configured correctly.");

      CamundaReactor.eventBus(engine);

    } finally {
      engine.close();
    }
  }

  @Test
  public void fails_to_get_eventBus_without_engine() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("No processEngine registered.");
    CamundaReactor.eventBus();
  }
}
