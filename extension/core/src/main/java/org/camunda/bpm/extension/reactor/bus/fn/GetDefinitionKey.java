package org.camunda.bpm.extension.reactor.bus.fn;


import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ProcessEngineServicesAware;

public final class GetDefinitionKey {

  public static String fromCaseExecution(DelegateCaseExecution execution) {
    return repositoryService(execution).getCaseDefinition(execution.getCaseDefinitionId()).getKey();
  }

  public static String fromTask(DelegateTask task) {
    if (task.getProcessDefinitionId() != null) {
      return repositoryService(task).getProcessDefinition(task.getProcessDefinitionId()).getKey();
    } else {
      return repositoryService(task).getCaseDefinition(task.getCaseDefinitionId()).getKey();
    }
  }

  public static String fromExecution(DelegateExecution execution) {
    return repositoryService(execution).getProcessDefinition(execution.getProcessDefinitionId()).getKey();
  }

  static RepositoryService repositoryService(ProcessEngineServicesAware processEngineServicesAware) {
    return processEngineServicesAware.getProcessEngineServices().getRepositoryService();
  }

  private GetDefinitionKey() {
    // util class, do not instantiate
  }
}
