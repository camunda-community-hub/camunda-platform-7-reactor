package org.camunda.bpm.extension.reactor.spring.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;
import org.camunda.bpm.extension.reactor.spring.CamundaReactorConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CamundaReactorConfiguration.class, ReactorExecutionListenerTestHelper.MyExecutionListener.class})
public class ReactorExecutionListenerTestHelper extends AbstractListenerTestHelper {

  @Component
  @CamundaSelector
  public static class MyExecutionListener extends ReactorExecutionListener {

    DelegateExecution received;

    @Override
    public void notify(final DelegateExecution execution) {
      received = execution;
    }
  }

  @Autowired
  private MyExecutionListener listener;


  @Test
  public void register() throws Exception {
    DelegateExecution execution = delegateExecution();

    camundaEventBus.notify(execution);

    assertThat(listener.received).isEqualTo(execution);
  }

}
