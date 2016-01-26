package org.camunda.bpm.extension.reactor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.extension.reactor.CamundaSelector.Queue.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.extension.reactor.listener.SubscriberExecutionListener;
import org.junit.Test;

public class CamundaSelectorTest {

  @CamundaSelector(queue = tasks, element = "e", event = "ev", process = "p", type = "t")
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
    assertThat(SelectorBuilder.selector(new Full() ).key()).isEqualTo("/camunda/tasks/t/p/e/ev");
  }

  @Test
  public void empty_selector_path() {
    assertThat(SelectorBuilder.selector(new None()).key()).isEqualTo("/camunda/none/{type}/{process}/{element}/{event}");
  }

}
