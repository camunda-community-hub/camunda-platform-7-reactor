package org.camunda.bpm.extension.reactor;

import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.jobexecutor.JobHandler;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.MockExpressionManager;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.ArrayList;

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

    this.getProcessEnginePlugins().add(new ReactorProcessEnginePlugin());
  }

}
