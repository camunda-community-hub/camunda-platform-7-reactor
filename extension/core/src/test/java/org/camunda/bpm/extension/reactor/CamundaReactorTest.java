package org.camunda.bpm.extension.reactor;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.bridge.SLF4JBridgeHandler;

import static org.assertj.core.api.Assertions.assertThat;

public class CamundaReactorTest {

  static {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
  }

  @Rule
  public final ExpectedException thrown = ExpectedException.none();



  @Test
  public void get_eventBus_from_engine() {
    ProcessEngine engine = new ReactorProcessEngineConfiguration().buildProcessEngine();

    try {
      assertThat(CamundaReactor.eventBus(engine)).isEqualTo(ReactorProcessEnginePlugin.CAMUNDA_EVENTBUS);

    } finally {
      engine.close();
    }
  }

  @Test
  public void get_eventBus_from_default_engine() {
    ProcessEngine engine = new ReactorProcessEngineConfiguration().buildProcessEngine();

    try {
      assertThat(CamundaReactor.eventBus()).isEqualTo(ReactorProcessEnginePlugin.CAMUNDA_EVENTBUS);
    } finally {
      engine.close();
    }
  }


  @Test
  public void fails_to_get_eventBus_from_engine() {
    ProcessEngine engine = new StandaloneInMemProcessEngineConfiguration(){{
      setDatabaseSchemaUpdate(ProcessEngineConfigurationImpl.DB_SCHEMA_UPDATE_DROP_CREATE);
    }}.buildProcessEngine();
    try {
      thrown.expect(IllegalStateException.class);
      thrown.expectMessage("No eventBus found. Make sure the Reactor plugin is configured correctly.");

      CamundaReactor.eventBus(engine);

    } finally {
      engine.close();
    }
  }

  @Test
  public void fails_to_get_eventBus_without_engine() {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("No processEngine registered.");
    CamundaReactor.eventBus();
  }
}
