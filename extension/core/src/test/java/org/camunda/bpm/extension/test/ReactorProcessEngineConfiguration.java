package org.camunda.bpm.extension.test;

import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.jobexecutor.JobHandler;
import org.camunda.bpm.engine.test.mock.MockExpressionManager;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import reactor.Environment;
import reactor.bus.EventBus;

import java.util.ArrayList;

public class ReactorProcessEngineConfiguration extends StandaloneInMemProcessEngineConfiguration {


  public ReactorProcessEngineConfiguration(EventBus eventBus) {
    this.history = HISTORY_FULL;
    this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_TRUE;
    this.jobExecutorActivate = false;
    this.expressionManager = new MockExpressionManager();

    this.setCustomPostBPMNParseListeners(new ArrayList<BpmnParseListener>());
    this.setCustomJobHandlers(new ArrayList<JobHandler>());
    this.setProcessEnginePlugins(new ArrayList<ProcessEnginePlugin>());

    this.getProcessEnginePlugins().add(new ReactorProcessEnginePlugin(eventBus));
  }

  public void addCustomJobHandler(final JobHandler jobHandler) {
    getCustomJobHandlers().add(jobHandler);
  }

  public void addCustomPostBpmnParseListener(final BpmnParseListener bpmnParseListener) {
    getCustomPostBPMNParseListeners().add(bpmnParseListener);
  }

  public void addProcessEnginePlugin(final ProcessEnginePlugin processEnginePlugin) {
    getProcessEnginePlugins().add(processEnginePlugin);
  }
}
