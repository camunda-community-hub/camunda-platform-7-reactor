package org.camunda.bpm.extension.reactor;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CamundaReactorTest {


  @Test
  public void camunda_topic_format() {
    assertThat(CamundaReactor.CAMUNDE_TOPIC_FORMAT).isEqualTo("/camunda/{0}/{1}/{2}");
  }

  @Test
  public void camunda_topic() {
    assertThat(CamundaReactor.CAMUNDE_TOPIC).isEqualTo("/camunda/{process}/{element}/{event}");
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
}
