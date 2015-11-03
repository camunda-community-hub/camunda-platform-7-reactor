package org.camunda.bpm.extension.test;

import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.jobexecutor.JobHandler;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.MockExpressionManager;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import reactor.Environment;
import reactor.bus.EventBus;

import java.util.ArrayList;

import static org.slf4j.LoggerFactory.getLogger;

public class ReactorProcessEngineConfiguration extends StandaloneInMemProcessEngineConfiguration {
    static {
      SLF4JBridgeHandler.removeHandlersForRootLogger();
      SLF4JBridgeHandler.install();
    }

  public static ProcessEngineRule buildRule() {

    final ReactorProcessEngineConfiguration configuration = new ReactorProcessEngineConfiguration();

    return new ProcessEngineRule(configuration.buildProcessEngine());
  }


  public ReactorProcessEngineConfiguration() {
    this.history = HISTORY_FULL;
    this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_DROP_CREATE;
    
    this.jobExecutorActivate = false;
    this.expressionManager = new MockExpressionManager();

    this.setCustomPostBPMNParseListeners(new ArrayList<BpmnParseListener>());
    this.setCustomJobHandlers(new ArrayList<JobHandler>());
    this.setProcessEnginePlugins(new ArrayList<ProcessEnginePlugin>());

    this.getProcessEnginePlugins().add(new ReactorProcessEnginePlugin());
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
