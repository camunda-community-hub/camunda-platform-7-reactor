package org.camunda.bpm.extension.reactor.bus.fn;


import org.camunda.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ProcessEngineServicesAware;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilderTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetDefinitionKeyTest {

  private static final String ID = "1";

  private final DelegateTask task = mock(DelegateTask.class);
  private final DelegateExecution execution= mock(DelegateExecution.class);
  private final DelegateCaseExecution caseExecution= mock(DelegateCaseExecution.class);

  private final RepositoryService repositoryService = mock(RepositoryService.class, RETURNS_DEEP_STUBS);

  @Before
  public void setUp() throws Exception {
    init(execution);
    init(caseExecution);
    init(task);
  }

  @Test
  public void fromExecution() throws Exception {
    when(execution.getProcessDefinitionId()).thenReturn("1");
    when(repositoryService.getProcessDefinition("1").getKey()).thenReturn("key");

    assertThat(GetDefinitionKey.fromExecution(execution)).isEqualTo("key");
  }

  @Test
  public void fromCaseExecution() throws Exception {
    when(caseExecution.getCaseDefinitionId()).thenReturn("1");
    when(repositoryService.getCaseDefinition("1").getKey()).thenReturn("key");

    assertThat(GetDefinitionKey.fromCaseExecution(caseExecution)).isEqualTo("key");
  }

  @Test
  public void fromTask_process() throws Exception {
    when(task.getProcessDefinitionId()).thenReturn(ID);
    when(task.getCaseDefinitionId()).thenReturn(null);
    when(repositoryService.getProcessDefinition(ID).getKey()).thenReturn("task-key");

    assertThat(GetDefinitionKey.fromTask(task)).isEqualTo("task-key");
  }


  @Test
  public void repositoryService() throws Exception {
    assertThat(GetDefinitionKey.repositoryService(task)).isEqualTo(repositoryService);
  }

  private void init(ProcessEngineServicesAware aware) {
    final ProcessEngineServices services = mock(ProcessEngineServices.class);
    when(aware.getProcessEngineServices()).thenReturn(services);
    when(services.getRepositoryService()).thenReturn(repositoryService);
  }
}
