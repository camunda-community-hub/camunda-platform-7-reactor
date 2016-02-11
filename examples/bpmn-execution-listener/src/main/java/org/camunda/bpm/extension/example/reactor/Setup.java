package org.camunda.bpm.extension.example.reactor;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;

public class Setup {

  private static ProcessEngineConfiguration CONFIGURATION = new StandaloneInMemProcessEngineConfiguration() {
    {
      this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_DROP_CREATE;
      this.getProcessEnginePlugins().add(CamundaReactor.plugin());
      this.jobExecutorActivate = false;
      this.isDbMetricsReporterActivate = false;
    }
  };

  private static ProcessEngine processEngine;

  public static ProcessEngine processEngine() {
    if (processEngine == null) {
      processEngine = CONFIGURATION.buildProcessEngine();
    }
    return processEngine;
  }

  public static void init() {
    CamundaEventBus eventBus = CamundaReactor.eventBus();
    // create and register listeners
    eventBus.register(new KpiExecutionListener());
  }

}
