package org.camunda.bpm.extension.example.reactor;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.complete;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.taskService;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.repositoryService;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;
import static org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin.CAMUNDA_EVENTBUS;

import java.util.ArrayList;
import java.util.Arrays;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.reactor.CamundaSelector;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import org.camunda.bpm.extension.reactor.listener.PublisherTaskListener;
import org.camunda.bpm.extension.reactor.listener.SubscriberTaskListener;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.Rule;
import org.junit.Test;

import reactor.bus.Event;
import reactor.bus.registry.Registration;
import reactor.bus.registry.Registry;
import reactor.fn.Consumer;

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
  @Deployment(resources = "ProcessA.bpmn")
  public void addCandidateGroup() {
    // register onCreate
    // new OnCreateListener();
    new TaskCreateListener(CAMUNDA_EVENTBUS);

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
