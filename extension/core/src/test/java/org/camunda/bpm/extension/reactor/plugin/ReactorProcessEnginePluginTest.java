package org.camunda.bpm.extension.reactor.plugin;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.ReactorProcessEngineConfiguration;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.camunda.bpm.extension.reactor.event.DelegateEventConsumer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.complete;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;
import static org.camunda.bpm.extension.reactor.CamundaReactor.selector;
import static org.slf4j.LoggerFactory.getLogger;


@Deployment(resources = {"ProcessA.bpmn"})
public class ReactorProcessEnginePluginTest {

  @Rule
  public final ProcessEngineRule processEngineRule = ReactorProcessEngineConfiguration.buildRule();

  private final Logger logger = getLogger(this.getClass());
  private CamundaEventBus eventBus;

  @Before
  public void init() {
    eventBus = CamundaReactor.eventBus(processEngineRule.getProcessEngine());
  }

  final Map<String, LinkedHashSet<DelegateEvent>> events = new LinkedHashMap<>();

  private final DelegateEventConsumer delegateEventConsumer = new DelegateEventConsumer() {
    @Override
    public void accept(DelegateEvent event) {
      LinkedHashSet<DelegateEvent> e = events.get(event.getKey());
      if (e == null) {
        events.put(event.getKey().toString(), new LinkedHashSet<DelegateEvent>());
        accept(event);
        return;
      }

      e.add(event);
    }
  };

  @Test
  public void fire_events_on_userTasks() {

    eventBus.register(selector().process("process_a"), delegateEventConsumer);
    eventBus.register(selector().process("process_a").element("task_a"), delegateEventConsumer);
    eventBus.register(selector().process("process_a").element("task_a").event("complete"), delegateEventConsumer);
    eventBus.register(selector().process("process_a").element("task_a").event("start"), delegateEventConsumer);
    eventBus.register(selector().process("process_a").element("task_a").event("end"), delegateEventConsumer);
    eventBus.register(selector().event("create"), delegateEventConsumer);
    eventBus.register(selector(), delegateEventConsumer);


    eventBus.register(selector().event("create"), new TaskListener() {
      @Override
      public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("foo");
        delegateTask.addCandidateGroup("bar");
        delegateTask.setName("my task");
      }
    });

    final ProcessInstance processInstance = processEngineRule.getRuntimeService().startProcessInstanceByKey("process_a");


    complete(task(processInstance));
    assertThat(task(processInstance)).isAssignedTo("foo");
    complete(task(processInstance));
    assertThat(processInstance).isEnded();

    for (Map.Entry<String, LinkedHashSet<DelegateEvent>> e : events.entrySet()) {
      logger.info("* " + e.getKey());
      for (DelegateEvent v : e.getValue()) {
        logger.info("    * " + v);
      }
    }

    assertThat(events).isNotEmpty();
  }

}
