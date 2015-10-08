package org.camunda.bpm.extension.reactor.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.slf4j.LoggerFactory;
import reactor.bus.EventBus;

import java.util.Arrays;
import java.util.List;

import static org.camunda.bpm.engine.delegate.ExecutionListener.EVENTNAME_END;
import static org.camunda.bpm.engine.delegate.ExecutionListener.EVENTNAME_START;
import static org.camunda.bpm.engine.delegate.ExecutionListener.EVENTNAME_TAKE;
import static org.camunda.bpm.engine.delegate.TaskListener.EVENTNAME_ASSIGNMENT;
import static org.camunda.bpm.engine.delegate.TaskListener.EVENTNAME_COMPLETE;
import static org.camunda.bpm.engine.delegate.TaskListener.EVENTNAME_CREATE;
import static org.camunda.bpm.engine.delegate.TaskListener.EVENTNAME_DELETE;

public class ReactorBpmnParseListener extends AbstractBpmnParseListener {

  static final List<String> TASK_EVENTS = Arrays.asList(EVENTNAME_ASSIGNMENT, EVENTNAME_COMPLETE, EVENTNAME_CREATE, EVENTNAME_DELETE);
  static final List<String> EXECUTION_EVENTS = Arrays.asList(EVENTNAME_START, EVENTNAME_TAKE, EVENTNAME_END);

  private final ReactorTaskListener taskListener;
  private final ReactorExecutionListener executionListener;

  public ReactorBpmnParseListener(final EventBus eventBus) {
    this(new ReactorTaskListener(eventBus), new ReactorExecutionListener(eventBus));
  }

  public ReactorBpmnParseListener(final ReactorTaskListener taskListener, final ReactorExecutionListener executionListener) {
    this.taskListener = taskListener;
    this.executionListener = executionListener;
  }


  @Override
  public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorTaskListener(taskDefinition(activity), taskListener);
  }

  static void addReactorTaskListener(TaskDefinition taskDefinition, ReactorTaskListener listener) {
    for (String event : TASK_EVENTS) {
      taskDefinition.addTaskListener(event, listener);
    }
  }

  /**
   * @param activity the taskActivity
   * @return taskDefinition for activity
   */
  private static TaskDefinition taskDefinition(final ActivityImpl activity) {
    final UserTaskActivityBehavior activityBehavior = (UserTaskActivityBehavior) activity.getActivityBehavior();
    return activityBehavior.getTaskDefinition();
  }

}
