package org.camunda.bpm.extension.example.reactor;

import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.joda.time.DateTime;
import reactor.bus.EventBus;

import java.util.Date;

public class ProcessA {

  public static final Date DUE_DATE = DateTime.now().plusDays(2).toDate();
  public static final String GROUP_1 = "group1";
  public static final String GROUP_2 = "group2";
  public static final String GROUP_3 = "group3";

  public static ProcessEngineConfiguration CONFIGURATION = new StandaloneInMemProcessEngineConfiguration() {
    {
      this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_TRUE;
      this.getProcessEnginePlugins().add(new ReactorProcessEnginePlugin());
      this.jobExecutorActivate = false;
    }
  };


  private final EventBus eventBus = CamundaReactor.eventBus();

  public void init() {
    new TaskCreateListener(eventBus);
    new TaskAssignListener();
  }

}
