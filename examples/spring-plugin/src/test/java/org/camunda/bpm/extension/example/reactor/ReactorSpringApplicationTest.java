package org.camunda.bpm.extension.example.reactor;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.cfg.CompositeProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReactorSpringApplication.class)
public class ReactorSpringApplicationTest {

  @Autowired
  private ProcessEngine processEngine;

  @Autowired
  private ReactorProcessEnginePlugin plugin;

  @Autowired
  private TaskService taskService;

  @Autowired
  private RuntimeService runtimeService;

  @Test
  public void contains_plugin() throws Exception {
    ProcessEngineConfigurationImpl configuration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
    CompositeProcessEnginePlugin composite = (CompositeProcessEnginePlugin) configuration.getProcessEnginePlugins().get(0);

    assertThat(composite.getPlugins()).contains(plugin);
  }

  @Test
  public void verify_assignment() throws Exception {
    final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process_a");

    final Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();

    assertThat(task.getAssignee()).isEqualTo("foo");
  }
}
