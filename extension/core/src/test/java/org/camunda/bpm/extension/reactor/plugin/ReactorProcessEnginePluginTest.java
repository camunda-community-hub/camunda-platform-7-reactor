package org.camunda.bpm.extension.reactor.plugin;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.reactor.CamundaReactor;
import org.camunda.bpm.extension.reactor.event.DelegateTaskEvent;
import org.camunda.bpm.extension.reactor.listener.SubscriberTaskListener;
import org.camunda.bpm.extension.test.ReactorProcessEngineConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import reactor.Environment;
import reactor.bus.EventBus;
import reactor.bus.selector.Selectors;
import reactor.core.dispatch.SynchronousDispatcher;
import reactor.fn.Consumer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.complete;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;
import static org.mockito.Mockito.spy;
import static org.slf4j.LoggerFactory.getLogger;

public class ReactorProcessEnginePluginTest {
  static {
    Environment.initializeIfEmpty().assignErrorJournal();
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
  }

  private final Logger logger = getLogger(this.getClass());

  private EventBus eventBus = spy(EventBus.create(new SynchronousDispatcher()));

  private final ReactorProcessEngineConfiguration configuration = new ReactorProcessEngineConfiguration(eventBus);

  @Rule
  public final ProcessEngineRule processEngineRule = new ProcessEngineRule(configuration.buildProcessEngine());

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  final Map<String, List<DelegateTaskEvent>> events = new LinkedHashMap<>();

  @Test
  @Deployment(resources = {"ProcessA.bpmn"})
  public void fire_events_on_userTasks() {

    register(CamundaReactor.topic("process_a", null, null));
    register(CamundaReactor.topic("process_a", "task_a", null));
    register(CamundaReactor.topic("process_a", "task_a", "complete"));
    register(CamundaReactor.topic(null, null, "create"));

    CamundaReactor.subscribeTo(eventBus).on(CamundaReactor.uri(null, null, "create"), SubscriberTaskListener.create(new TaskListener() {
      @Override
      public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("foo");
        delegateTask.addCandidateGroup("bar");
        delegateTask.setName("my task");
      }
    }));

    final ProcessInstance processInstance = processEngineRule.getRuntimeService().startProcessInstanceByKey("process_a");


    complete(task(processInstance));
    assertThat(task(processInstance)).isAssignedTo("foo");
    complete(task(processInstance));
    assertThat(processInstance).isEnded();

    assertThat(events).isNotEmpty();

    for (Map.Entry<String, List<DelegateTaskEvent>> e : events.entrySet()) {
      logger.info("* " + e.getKey());
      for (DelegateTaskEvent v : e.getValue()) {
        logger.info("    * " + v);
      }
    }
    assertThat(events.get("/camunda/process_a/{element}/{event}")).hasSize(6);

  }

  private void register(String uri) {
    eventBus.on(Selectors.uri(uri), consumer(uri));
  }

  private Consumer<DelegateTaskEvent> consumer(final String uri) {
    return new Consumer<DelegateTaskEvent>() {
      @Override
      public void accept(DelegateTaskEvent delegateTaskEvent) {
        List<DelegateTaskEvent> e = events.get(uri);
        if (e == null) {
          e = new ArrayList<>();
          events.put(uri, e);
        }
        e.add(delegateTaskEvent);
      }
    };
  }
}
