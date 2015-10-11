package org.camunda.bpm.extension.reactor;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.extension.test.ReactorProcessEngineConfiguration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.bridge.SLF4JBridgeHandler;
import reactor.core.dispatch.SynchronousDispatcher;

import static org.assertj.core.api.Assertions.assertThat;

public class CamundaReactorTest {

  static {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
  }

  @Rule
  public final ExpectedException thrown = ExpectedException.none();


  @Test
  public void camunda_topic() {
    assertThat(CamundaReactor.CAMUNDA_TOPIC).isEqualTo("/camunda/{process}/{element}/{event}");
  }

  @Test
  public void creates_topic_for_process_element_and_event() {
    assertThat(CamundaReactor.topic("process", "task", "create")).isEqualTo("/camunda/process/task/create");
  }

  @Test
  public void creates_general_topic_for_null_values() {
    assertThat(CamundaReactor.topic(null, null, null)).isEqualTo("/camunda/{process}/{element}/{event}");
  }

  @Test
  public void creates_topic_for_element() {
    assertThat(CamundaReactor.topic(null, "task", null)).isEqualTo("/camunda/{process}/task/{event}");
  }

  @Test
  public void creates_topic_for_process() {
    assertThat(CamundaReactor.topic("foo", null, null)).isEqualTo("/camunda/foo/{element}/{event}");
  }

  @Test
  public void creates_topic_for_event() {
    assertThat(CamundaReactor.topic(null, null, "bar")).isEqualTo("/camunda/{process}/{element}/bar");
  }

  @Test
  public void retrieve_processDefinitionKey_from_definitionId() {
    assertThat(CamundaReactor.processDefintionKey("process_a:1:3")).isEqualTo("process_a");
  }

  @Test
  public void get_eventBus_from_engine() {
    ProcessEngine engine = new ReactorProcessEngineConfiguration().buildProcessEngine();

    assertThat(CamundaReactor.getEventBus(engine).getDispatcher()).isEqualTo(SynchronousDispatcher.INSTANCE);

    engine.close();
  }
  @Test
  public void fails_to_get_eventBus_from_engine() {
    ProcessEngine engine = new StandaloneInMemProcessEngineConfiguration().buildProcessEngine();
    try {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("No eventBus found. Make sure the Reactor plugin is configured correctly.");

    CamundaReactor.getEventBus(engine);

    } finally {
    engine.close();

    }


  }
}
