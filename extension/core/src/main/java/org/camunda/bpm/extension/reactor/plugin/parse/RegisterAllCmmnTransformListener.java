package org.camunda.bpm.extension.reactor.plugin.parse;

import static org.camunda.bpm.engine.delegate.CaseExecutionListener.*;
import static org.camunda.bpm.engine.delegate.TaskListener.*;

import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.engine.delegate.CaseExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.cmmn.behavior.HumanTaskActivityBehavior;
import org.camunda.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.engine.impl.cmmn.transformer.AbstractCmmnTransformListener;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.model.cmmn.impl.instance.CasePlanModel;
import org.camunda.bpm.model.cmmn.instance.*;

public class RegisterAllCmmnTransformListener extends AbstractCmmnTransformListener {

  static final List<String> TASK_EVENTS = RegisterAllBpmnParseListener.TASK_EVENTS;
  static final List<String> LIFECYCLE_EVENTS = Arrays.asList(
    CREATE,
    ENABLE,
    DISABLE,
    RE_ENABLE,
    START,
    MANUAL_START,
    COMPLETE,
    RE_ACTIVATE,
    TERMINATE,
    EXIT,
    PARENT_TERMINATE,
    SUSPEND,
    RESUME,
    PARENT_SUSPEND,
    PARENT_RESUME,
    CLOSE,
    OCCUR
    );

  private final CaseExecutionListener caseExecutionListener;
  private final TaskListener taskListener;

  public RegisterAllCmmnTransformListener(final TaskListener taskListener, final CaseExecutionListener caseExecutionListener) {
    this.taskListener = taskListener;
    this.caseExecutionListener = caseExecutionListener;
  }

  @Override
  public void transformHumanTask(PlanItem planItem, HumanTask humanTask, CmmnActivity activity) {
    addDelegateListener(activity);
    addTaskListener(taskDefinition(activity));
  }

  @Override
  public void transformProcessTask(PlanItem planItem, ProcessTask processTask, CmmnActivity activity) {
    addDelegateListener(activity);
  }

  @Override
  public void transformCaseTask(PlanItem planItem, CaseTask caseTask, CmmnActivity activity) {
    addDelegateListener(activity);
  }

  @Override
  public void transformStage(PlanItem planItem, Stage stage, CmmnActivity activity) {
    addDelegateListener(activity);
  }

  @Override
  public void transformMilestone(PlanItem planItem, Milestone milestone, CmmnActivity activity) {
    addDelegateListener(activity);
  }

  @Override
  public void transformEventListener(PlanItem planItem, EventListener eventListener, CmmnActivity activity) {
    addDelegateListener(activity);
  }

  @Override
  public void transformCasePlanModel(CasePlanModel casePlanModel, CmmnActivity activity) {
    addDelegateListener(activity);
  }


  void addDelegateListener(final CmmnActivity activity) {
    for(String event: LIFECYCLE_EVENTS) {
      activity.addListener(event, caseExecutionListener);
    }
  }

  void addTaskListener(TaskDefinition taskDefinition) {
    for (String event : TASK_EVENTS) {
      taskDefinition.addTaskListener(event, taskListener);
    }
  }

  /**
   * @param activity the taskActivity
   * @return taskDefinition for activity
   */
  static TaskDefinition taskDefinition(final CmmnActivity activity) {
    final HumanTaskActivityBehavior activityBehavior = (HumanTaskActivityBehavior) activity.getActivityBehavior();
    return activityBehavior.getTaskDefinition();
  }
}
