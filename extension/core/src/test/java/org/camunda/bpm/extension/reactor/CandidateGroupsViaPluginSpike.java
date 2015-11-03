package org.camunda.bpm.extension.reactor;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.complete;
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
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.Test;

import reactor.bus.selector.Selectors;
import reactor.fn.Consumer;

/**
 * Spike that assures that the general approach is working.
 */
public class CandidateGroupsViaPluginSpike {

  /**
   * Publish delegateTaskEvent to all.
   */
  private final TaskListener publishTaskCreate = new TaskListener() {
    
    @Override
    public void notify(DelegateTask delegateTask) {
      CAMUNDA_EVENTBUS.notify(Selectors.matchAll(), new DelegateTaskEvent(delegateTask));
    }
  };
  
  /**
   * Plugin with PostParseListener that registers "publishTaskCreate".
   */
  private final ProcessEnginePlugin plugin = new AbstractProcessEnginePlugin(){
    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
      processEngineConfiguration.getCustomPostBPMNParseListeners().add(new AbstractBpmnParseListener(){
        @Override
        public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
          TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activity.getActivityBehavior()).getTaskDefinition();
          taskDefinition.addTaskListener(TaskListener.EVENTNAME_CREATE, publishTaskCreate);
        }
      });
    }
  };
  
  /**
   * Adds candidate groups on create.
   */
  private final Consumer<DelegateTaskEvent> onCreate = new  Consumer<DelegateTaskEvent>() {

    @Override
    public void accept(DelegateTaskEvent delegateTaskEvent) {
      DelegateTask delegateTask = delegateTaskEvent.getData();
      delegateTask.addCandidateGroup("group");
      delegateTask.addCandidateGroups(Arrays.asList("foo","bar"));
    }
  };

  /**
   * Configuration with plugin.
   */
  private final ProcessEngineConfigurationImpl processEngineConfiguration = new StandaloneInMemProcessEngineConfiguration() {{
    setDatabaseSchemaUpdate(ProcessEngineConfigurationImpl.DB_SCHEMA_UPDATE_DROP_CREATE);
    setCustomPostBPMNParseListeners(new ArrayList<BpmnParseListener>());
    getProcessEnginePlugins().add(plugin);
  }};

  
  /**
   * Small process with on user task.
   */
  private final BpmnModelInstance modelInstance = Bpmn.createExecutableProcess("process")
      .startEvent()
      .userTask("task1").name("Do something")
      .endEvent()
      .done();


  @Test
  public void addCandidateGroup() {
    // register onCreate
    CAMUNDA_EVENTBUS.on(Selectors.matchAll(), onCreate);
    
    // create process Engine
    processEngineConfiguration.buildProcessEngine();
    
    repositoryService().createDeployment().addModelInstance("process.bpmn", modelInstance).deploy();

    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("process");

    assertThat(processInstance).isWaitingAt("task1");
    assertThat(task()).hasCandidateGroup("group");
    assertThat(task()).hasCandidateGroup("foo");
    assertThat(task()).hasCandidateGroup("bar");
    
    complete(task());
    
    assertThat(processInstance).isEnded();
  }
}
