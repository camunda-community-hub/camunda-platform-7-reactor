package org.camunda.bpm.extension.reactor;

import org.camunda.bpm.extension.reactor.listener.SubscriberExecutionListener;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CamundaSelectorTest {

  @CamundaSelector(element = "e", event = "ev", process = "p", type = "t")
  public static abstract class Full extends SubscriberExecutionListener {

  }

  @CamundaSelector
  public static abstract class None extends SubscriberExecutionListener {

  }

  @Test
  public void full_selector_path() {
    assertThat(SelectorBuilder.selector(Full.class).createTopic()).isEqualTo("/camunda/t/p/e/ev");
  }

  @Test
  public void empty_selector_path() {
    assertThat(SelectorBuilder.selector(None.class).createTopic()).isEqualTo("/camunda/{type}/{process}/{element}/{event}");
  }

}
