package org.camunda.bpm.extension.example.reactor;

import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;
import org.camunda.bpm.extension.reactor.spring.CamundaReactorConfiguration;
import org.camunda.bpm.extension.reactor.spring.listener.ReactorTaskListener;
import org.camunda.bpm.spring.boot.starter.SpringBootProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ProcessApplication
public class ReactorSpringApplication extends SpringBootProcessApplication {

  public static void main(final String... args) {
    SpringApplication.run(ReactorSpringApplication.class, args);
  }

  @Component
  @CamundaSelector(event = TaskListener.EVENTNAME_CREATE)
  public static class MyTaskCreateListener extends ReactorTaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
      delegateTask.setAssignee("foo");
    }
  }

}
