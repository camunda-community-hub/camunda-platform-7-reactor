package org.camunda.bpm.extension.example.reactor;

import com.google.common.eventbus.Subscribe;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ActivityStartBehavior;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.assertions.ProcessEngineTests;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.Selectors;
import reactor.fn.Consumer;

import java.util.ArrayList;
import java.util.Arrays;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.repositoryService;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;

public class CandidateGroupsInPluginTest extends AbstractProcessEnginePlugin {

  private static final EventBus EVENT_BUS = ReactorProcessEnginePlugin.CAMUNDA_EVENTBUS;

  private static final String GROUP = "group";

  public static class OnCreate implements Consumer<DelegateTaskEvent> {

    @Override
    public void accept(DelegateTaskEvent delegateTaskEvent) {
      DelegateTask delegateTask = delegateTaskEvent.getData();
      delegateTask.addCandidateGroup(GROUP);
      delegateTask.addCandidateGroups(Arrays.asList("foo","bar"));
    }
  }

  private static final TaskListener ADD_CANDIDATE_GROUP = new TaskListener() {
    @Override
    public void notify(DelegateTask delegateTask) {
      EVENT_BUS.notify(Selectors.matchAll(), new DelegateTaskEvent(delegateTask));
    }
  };

  private final ProcessEngineConfigurationImpl processEngineConfiguration = new StandaloneInMemProcessEngineConfiguration();
  private BpmnModelInstance modelInstance;

  @Before
  public void setUp() {
    EVENT_BUS.on(Selectors.matchAll(), new OnCreate());

    processEngineConfiguration.setCustomPostBPMNParseListeners(new ArrayList<BpmnParseListener>());
    processEngineConfiguration.getProcessEnginePlugins().add(this);

    modelInstance = Bpmn.createExecutableProcess("process")
      .startEvent()
      .userTask("task1").name("Do something")
      .endEvent()
      .done();
  }


  @Test
  public void addCandidateGroup() {
    final ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
    repositoryService().createDeployment().addModelInstance("process.bpmn", modelInstance).deploy();

    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("process");

    assertThat(processInstance).isWaitingAt("task1");
    assertThat(task()).hasCandidateGroup(GROUP);
    assertThat(task()).hasCandidateGroup("foo");
    assertThat(task()).hasCandidateGroup("bar");
  }


  @Override
  public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
    processEngineConfiguration.getCustomPostBPMNParseListeners().add(new AbstractBpmnParseListener(){
      @Override
      public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
        taskDefinition(activity).addTaskListener(TaskListener.EVENTNAME_CREATE, ADD_CANDIDATE_GROUP);
      }
    });
  }

  private static TaskDefinition taskDefinition(ActivityImpl activity) {
    return ((UserTaskActivityBehavior) activity.getActivityBehavior()).getTaskDefinition();
  }
}
