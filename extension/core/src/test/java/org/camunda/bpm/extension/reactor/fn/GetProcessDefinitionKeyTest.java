package org.camunda.bpm.extension.reactor.fn;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.repository.CaseDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class GetProcessDefinitionKeyTest {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();
  @Rule
  public final MockitoRule mockito = MockitoJUnit.rule();

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private DelegateTask task;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private DelegateCaseExecution caseExecution;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private DelegateExecution execution;

  @Mock
  private RepositoryService repositoryService;

  @Before
  public void setUp() throws Exception {
    when(task.getProcessEngineServices().getRepositoryService()).thenReturn(repositoryService);
    when(caseExecution.getProcessEngineServices().getRepositoryService()).thenReturn(repositoryService);
    when(execution.getProcessEngineServices().getRepositoryService()).thenReturn(repositoryService);
  }

  @Test
  public void get_from_task_via_process() throws Exception {
    processDefinition("1", "proc");
    when(task.getProcessDefinitionId()).thenReturn("1");

    assertThat(GetProcessDefinitionKey.from(task)).isEqualTo("proc");
  }

  @Test
  public void get_from_task_via_case() throws Exception {
    caseDefinition("1", "case");
    when(task.getCaseDefinitionId()).thenReturn("1");

    assertThat(GetProcessDefinitionKey.from(task)).isEqualTo("case");
  }

  @Test
  public void cannot_load_process_for_task() throws Exception {
    when(task.getProcessDefinitionId()).thenReturn("not there!");

    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("bpmn/not there!");

    GetProcessDefinitionKey.from(task);

  }

  @Test
  public void cannot_load_case_for_task() throws Exception {
    when(task.getCaseDefinitionId()).thenReturn("not there!");

    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("cmmn/not there!");

    GetProcessDefinitionKey.from(task);
  }

  @Test
  public void get_from_caseExecution() throws Exception {
    caseDefinition("1", "case");
    when(caseExecution.getCaseDefinitionId()).thenReturn("1");

    assertThat(GetProcessDefinitionKey.from(caseExecution)).isEqualTo("case");
  }

  @Test
  public void get_from_execution() throws Exception {
    processDefinition("1", "proc");
    when(execution.getProcessDefinitionId()).thenReturn("1");

    assertThat(GetProcessDefinitionKey.from(execution)).isEqualTo("proc");
  }

  private CaseDefinition caseDefinition(String id, String key) {
    CaseDefinition caseDefinition = mock(CaseDefinition.class);
    when(caseDefinition.getId()).thenReturn(id);
    when(caseDefinition.getKey()).thenReturn(key);
    when(repositoryService.getCaseDefinition(id)).thenReturn(caseDefinition);
    return caseDefinition;
  }

  private ProcessDefinition processDefinition(String id, String key) {
    ProcessDefinition processDefinition = mock(ProcessDefinition.class);
    when(processDefinition.getId()).thenReturn(id);
    when(processDefinition.getKey()).thenReturn(key);
    when(repositoryService.getProcessDefinition(id)).thenReturn(processDefinition);
    return processDefinition;
  }

}
