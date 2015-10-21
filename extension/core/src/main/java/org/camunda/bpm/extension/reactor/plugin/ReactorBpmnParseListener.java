package org.camunda.bpm.extension.reactor.plugin;

import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.pvm.process.TransitionImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.camunda.bpm.engine.impl.variable.VariableDeclaration;
import org.camunda.bpm.extension.reactor.listener.PublisherExecutionListener;
import org.camunda.bpm.extension.reactor.listener.PublisherTaskListener;
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
  static final List<String> EXECUTION_EVENTS = Arrays.asList(EVENTNAME_START, EVENTNAME_END);

  private final PublisherTaskListener taskListener;
  private final PublisherExecutionListener executionListener;

  public ReactorBpmnParseListener(final EventBus eventBus) {
    this(new PublisherTaskListener(eventBus), new PublisherExecutionListener(eventBus));
  }

  public ReactorBpmnParseListener(final PublisherTaskListener taskListener, final PublisherExecutionListener executionListener) {
    this.taskListener = taskListener;
    this.executionListener = executionListener;
  }

  @Override
  public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorTaskListener(taskDefinition(activity));
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseBoundaryErrorEventDefinition(Element errorEventDefinition, boolean interrupting, ActivityImpl activity, ActivityImpl nestedErrorEventActivity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseBoundaryEvent(Element boundaryEventElement, ScopeImpl scopeElement, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseBoundaryMessageEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseBoundarySignalEventDefinition(Element signalEventDefinition, boolean interrupting, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseBoundaryTimerEventDefinition(Element timerEventDefinition, boolean interrupting, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseBusinessRuleTask(Element businessRuleTaskElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseCallActivity(Element callActivityElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseCompensateEventDefinition(Element compensateEventDefinition, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseEndEvent(Element endEventElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseEventBasedGateway(Element eventBasedGwElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseExclusiveGateway(Element exclusiveGwElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseInclusiveGateway(Element inclusiveGwElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseIntermediateCatchEvent(Element intermediateEventElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseIntermediateMessageCatchEventDefinition(Element messageEventDefinition, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseIntermediateSignalCatchEventDefinition(Element signalEventDefinition, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseIntermediateThrowEvent(Element intermediateEventElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseIntermediateTimerEventDefinition(Element timerEventDefinition, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseManualTask(Element manualTaskElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseMultiInstanceLoopCharacteristics(Element activityElement, Element multiInstanceLoopCharacteristicsElement, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseParallelGateway(Element parallelGwElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseProcess(Element processElement, ProcessDefinitionEntity processDefinition) {
    // FIXME: is it a good idea to implement genenric global process listeners?
  }

  @Override
  public void parseReceiveTask(Element receiveTaskElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }


  @Override
  public void parseScriptTask(Element scriptTaskElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseSendTask(Element sendTaskElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseSequenceFlow(Element sequenceFlowElement, ScopeImpl scopeElement, TransitionImpl transition) {
    addReactorExecutionListener(transition);
  }

  @Override
  public void parseServiceTask(Element serviceTaskElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseStartEvent(Element startEventElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseSubProcess(Element subProcessElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseTask(Element taskElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  @Override
  public void parseTransaction(Element transactionElement, ScopeImpl scope, ActivityImpl activity) {
    addReactorExecutionListener(activity);
  }

  void addReactorExecutionListener(final ActivityImpl activity) {
    for (String event : EXECUTION_EVENTS) {
      activity.addListener(event, executionListener);
    }
  }

  void addReactorExecutionListener(final TransitionImpl transition) {
    transition.addListener(EVENTNAME_TAKE, executionListener);
  }

  void addReactorTaskListener(TaskDefinition taskDefinition) {
    for (String event : TASK_EVENTS) {
      taskDefinition.addTaskListener(event, taskListener);
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
