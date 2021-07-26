package org.camunda.bpm.extension.reactor.plugin.parse;

import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.pvm.process.TransitionImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.camunda.bpm.engine.delegate.ExecutionListener.*;
import static org.camunda.bpm.engine.delegate.TaskListener.*;

public class RegisterAllBpmnParseListener extends AbstractBpmnParseListener {

  private final static Logger log = LoggerFactory.getLogger(RegisterAllBpmnParseListener.class);

  private final boolean reactorListenerFirstOnUserTask;

  public static final List<String> TASK_EVENTS = Arrays.asList(
    EVENTNAME_COMPLETE,
    EVENTNAME_ASSIGNMENT,
    EVENTNAME_CREATE,
    EVENTNAME_UPDATE,
    EVENTNAME_TIMEOUT,
    EVENTNAME_DELETE);
  public static final List<String> EXECUTION_EVENTS = Arrays.asList(
    EVENTNAME_START,
    EVENTNAME_END);

  private final TaskListener taskListener;
  private final ExecutionListener executionListener;

  public RegisterAllBpmnParseListener(final TaskListener taskListener, final ExecutionListener executionListener, final boolean reactorListenerFirstOnUserTask) {
    this.taskListener = taskListener;
    this.executionListener = executionListener;
    this.reactorListenerFirstOnUserTask = reactorListenerFirstOnUserTask;

    log.info("Move reactor listeners to top for user tasks = {}", this.reactorListenerFirstOnUserTask);
  }

  @Override
  public void parseUserTask(final Element userTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
    addTaskListener(taskDefinition(activity));
    addExecutionListener(activity);
  }

  @Override
  public void parseBoundaryErrorEventDefinition(final Element errorEventDefinition, final boolean interrupting, final ActivityImpl activity, final ActivityImpl nestedErrorEventActivity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseBoundaryEvent(final Element boundaryEventElement, final ScopeImpl scopeElement, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseBoundaryMessageEventDefinition(final Element element, final boolean interrupting, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseBoundarySignalEventDefinition(final Element signalEventDefinition, final boolean interrupting, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseBoundaryTimerEventDefinition(final Element timerEventDefinition, final boolean interrupting, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseBusinessRuleTask(final Element businessRuleTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseCallActivity(final Element callActivityElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseCompensateEventDefinition(final Element compensateEventDefinition, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseEndEvent(final Element endEventElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseEventBasedGateway(final Element eventBasedGwElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseExclusiveGateway(final Element exclusiveGwElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseInclusiveGateway(final Element inclusiveGwElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseIntermediateCatchEvent(final Element intermediateEventElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseIntermediateMessageCatchEventDefinition(final Element messageEventDefinition, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseIntermediateSignalCatchEventDefinition(final Element signalEventDefinition, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseIntermediateThrowEvent(final Element intermediateEventElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseIntermediateTimerEventDefinition(final Element timerEventDefinition, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseManualTask(final Element manualTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseMultiInstanceLoopCharacteristics(final Element activityElement, final Element multiInstanceLoopCharacteristicsElement, final ActivityImpl activity) {
    // DO NOT IMPLEMENT!
    // we do not notify on entering a multi-instance activity, this will be done for every single execution inside that loop.
  }

  @Override
  public void parseParallelGateway(final Element parallelGwElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseProcess(final Element processElement, final ProcessDefinitionEntity processDefinition) {
    // FIXME: is it a good idea to implement genenric global process listeners?
  }

  @Override
  public void parseReceiveTask(final Element receiveTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseScriptTask(final Element scriptTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseSendTask(final Element sendTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseSequenceFlow(final Element sequenceFlowElement, final ScopeImpl scopeElement, final TransitionImpl transition) {
    addExecutionListener(transition);
  }

  @Override
  public void parseServiceTask(final Element serviceTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseStartEvent(final Element startEventElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseSubProcess(final Element subProcessElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseTask(final Element taskElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  @Override
  public void parseTransaction(final Element transactionElement, final ScopeImpl scope, final ActivityImpl activity) {
    addExecutionListener(activity);
  }

  void addExecutionListener(final ActivityImpl activity) {
    for (final String event : EXECUTION_EVENTS) {
      activity.addListener(event, executionListener);
    }
  }

  void addExecutionListener(final TransitionImpl transition) {
    transition.addListener(EVENTNAME_TAKE, executionListener);
  }

  void addTaskListener(final TaskDefinition taskDefinition) {
    if(reactorListenerFirstOnUserTask) {
      taskDefinition.setTaskListeners(reactorListenerFirstMap(taskDefinition.getTaskListeners(), TASK_EVENTS, taskListener));
    } else {
      for (final String event : TASK_EVENTS) {
        taskDefinition.addTaskListener(event, taskListener);
      }
    }
  }

  /**
   * Adds the reactor listener to the first place of each event listener list
   */
  private static <T> Map<String, List<T>> reactorListenerFirstMap(final Map<String, List<T>> origListenerMap, final List<String> events, final T listener) {
    final Map<String, List<T>> taskListenersReOrdered = new HashMap<>();

    for (final String event : events) {
      final List<T> taskListenerListReOrdered = new ArrayList<>();
      taskListenerListReOrdered.add(listener);
      if(origListenerMap.get(event) != null && !origListenerMap.get(event).isEmpty()) {
        taskListenerListReOrdered.addAll(origListenerMap.get(event));
      }
      taskListenersReOrdered.put(event, taskListenerListReOrdered);
    }

    return taskListenersReOrdered;
  }

  /**
   * @param activity the taskActivity
   * @return taskDefinition for activity
   */
  static TaskDefinition taskDefinition(final ActivityImpl activity) {
    final UserTaskActivityBehavior activityBehavior = (UserTaskActivityBehavior) activity.getActivityBehavior();
    return activityBehavior.getTaskDefinition();
  }
}
