package org.camunda.bpm.extension.reactor.fn;

import static org.camunda.bpm.extension.reactor.bus.SelectorBuilder.Context.cmmn;

import java.util.Optional;
import java.util.function.Supplier;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateCaseExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.repository.CaseDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder.Context;

public class GetProcessDefinitionKey {

  public static String from(final DelegateTask delegateTask) {
    Context context = null;
    String id = null;
    if (delegateTask.getProcessDefinitionId() != null) {
      context = Context.bpmn;
      id = delegateTask.getProcessDefinitionId();
    } else {
      context = cmmn;
      id = delegateTask.getCaseDefinitionId();
    }

    return new Adapter(context, delegateTask.getProcessEngineServices().getRepositoryService(), id).get();
  }

  public static String from(DelegateCaseExecution execution) {
    return new Adapter(cmmn, execution.getProcessEngineServices().getRepositoryService(), execution.getCaseDefinitionId()).get();
  }

  public static String from(DelegateExecution execution) {
    return new Adapter(Context.bpmn, execution.getProcessEngineServices().getRepositoryService(), execution.getProcessDefinitionId()).get();
  }


  private static class Adapter implements Supplier<String> {

    private String key;

    private Adapter(Context context, RepositoryService repositoryService, String id) {

      Optional<String> key = Optional.empty();

      switch (context) {
      case bpmn:
        key = Optional.ofNullable(repositoryService.getProcessDefinition(id)).map(ProcessDefinition::getKey);
        break;
      case cmmn:
        key = Optional.ofNullable(repositoryService.getCaseDefinition(id)).map(CaseDefinition::getKey);
        break;
      }
      this.key = key.orElseThrow(() -> new IllegalStateException(String.format("could not load definition for %s/%s", context, id)));
    }

    @Override
    public String get() {
      return key;
    }
  }

  private GetProcessDefinitionKey() {
    // do not instantiate, util class
  }

}
