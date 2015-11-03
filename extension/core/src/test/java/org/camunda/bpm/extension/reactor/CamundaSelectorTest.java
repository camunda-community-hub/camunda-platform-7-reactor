package org.camunda.bpm.extension.reactor;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.extension.reactor.listener.SubscriberExecutionListener;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CamundaSelectorTest {

  @CamundaSelector(element = "e", event = "ev", process = "p", type = "t")
  public static  class Full extends SubscriberExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
      // TODO Auto-generated method stub
      
    }

  }

  @CamundaSelector
  public static  class None extends SubscriberExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
      // TODO Auto-generated method stub
      
    }

  }

  @Test
  public void full_selector_path() {
    assertThat(SelectorBuilder.selector(new Full() ).key()).isEqualTo("/camunda/t/p/e/ev");
  }

  @Test
  public void empty_selector_path() {
    assertThat(SelectorBuilder.selector(new None()).key()).isEqualTo("/camunda/{type}/{process}/{element}/{event}");
  }

}
