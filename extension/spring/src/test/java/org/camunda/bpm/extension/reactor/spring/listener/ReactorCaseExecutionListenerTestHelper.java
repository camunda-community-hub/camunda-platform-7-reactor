package org.camunda.bpm.extension.reactor.spring.listener;

import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
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
@ContextConfiguration(classes = {CamundaReactorConfiguration.class, ReactorCaseExecutionListenerTestHelper.MyCaseExecutionListener.class})
public class ReactorCaseExecutionListenerTestHelper extends AbstractListenerTestHelper {

  @Component
  @CamundaSelector
  public static class MyCaseExecutionListener extends ReactorCaseExecutionListener {

    DelegateCaseExecution received;

    @Override
    public void notify(final DelegateCaseExecution execution) {
      received = execution;
    }
  }

  @Autowired
  private ReactorCaseExecutionListenerTestHelper.MyCaseExecutionListener listener;


  @Test
  public void register() throws Exception {
    DelegateCaseExecution execution = delegateCaseExecution();

    camundaEventBus.notify(execution);

    assertThat(listener.received).isEqualTo(execution);
  }

}
