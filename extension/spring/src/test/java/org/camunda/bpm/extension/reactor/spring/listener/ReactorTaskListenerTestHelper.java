package org.camunda.bpm.extension.reactor.spring.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
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
@ContextConfiguration(classes = {CamundaReactorConfiguration.class, ReactorTaskListenerTestHelper.MyTaskListener.class})
public class ReactorTaskListenerTestHelper extends AbstractListenerTestHelper {

  @Component
  @CamundaSelector
  public static class MyTaskListener extends ReactorTaskListener {

    DelegateTask received;

    @Override
    public void notify(final DelegateTask delegateTask) {
      received = delegateTask;
    }
  }

  @Autowired
  private MyTaskListener myTaskListener;


  @Test
  public void register() throws Exception {
    assertThat(myTaskListener).isNotNull();

    DelegateTask task = delegateTask();

    camundaEventBus.notify(task);

    assertThat(myTaskListener.received).isEqualTo(task);
  }

}
