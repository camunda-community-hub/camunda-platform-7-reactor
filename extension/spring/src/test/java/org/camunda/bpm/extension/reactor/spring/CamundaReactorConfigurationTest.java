package org.camunda.bpm.extension.reactor.spring;

import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CamundaReactorConfiguration.class)
public class CamundaReactorConfigurationTest {

  @Autowired
  private CamundaEventBus bus;

  @Autowired
  private ReactorProcessEnginePlugin plugin;

  @Test
  public void inject_sucessfully() {
    assertThat(plugin.getEventBus()).isEqualTo(bus);
  }
}
