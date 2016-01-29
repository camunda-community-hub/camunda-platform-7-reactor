package org.camunda.bpm.extension.example.reactor;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.complete;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;
import static org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin.CAMUNDA_EVENTBUS;

import java.util.Arrays;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;
import org.camunda.bpm.extension.reactor.listener.SubscriberTaskListener;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

/**
 * Spike that assures that the general approach is working.
 */
public class CandidateGroupsViaPluginSpike {

  @CamundaSelector(type = "userTask", event = "create")
  public static class OnCreateListener extends SubscriberTaskListener {

    public OnCreateListener() {
      register(CAMUNDA_EVENTBUS);
    }

    @Override
    public void notify(DelegateTask delegateTask) {
      delegateTask.addCandidateGroup(ProcessA.GROUP_1);
      delegateTask.addCandidateGroups(Arrays.asList(ProcessA.GROUP_2, ProcessA.GROUP_3));
    }

  }

  @Rule
  public final ProcessEngineRule processEngineRule = new ProcessEngineRule(ProcessA.CONFIGURATION.buildProcessEngine());

  @Test
  @Ignore
  @Deployment(resources = "ProcessA.bpmn")
  public void addCandidateGroup() {
     new OnCreateListener();

    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("process_a");

    assertThat(processInstance).isWaitingAt("task_a");

    assertThat(task()).hasCandidateGroup("group1");
    assertThat(task()).hasCandidateGroup("group2");
    assertThat(task()).hasCandidateGroup("group3");

    complete(task());

    assertThat(processInstance).isWaitingAt("task_b");
    assertThat(task()).hasCandidateGroup("group1");
    assertThat(task()).hasCandidateGroup("group2");
    assertThat(task()).hasCandidateGroup("group3");

    complete(task());

    assertThat(processInstance).isEnded();
  }
}
