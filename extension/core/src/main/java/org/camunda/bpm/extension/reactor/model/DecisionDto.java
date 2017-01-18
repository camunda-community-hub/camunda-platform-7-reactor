package org.camunda.bpm.extension.reactor.model;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.dmn.engine.DmnDecision;

public class DecisionDto extends AbstractDto {
  private static final long serialVersionUID = 1L;

  public static DecisionDto from(DmnDecision decision) {
    final DecisionDto dto = new DecisionDto();

    dto.key = decision.getKey();
    dto.name = decision.getName();
    dto.isDecisionTable = decision.isDecisionTable();
    for (DmnDecision d : decision.getRequiredDecisions()) {
      dto.requiredDecisions.add(from(d));
    }

    return dto;
  }

  protected boolean isDecisionTable;
  protected List<DecisionDto> requiredDecisions = new ArrayList<>();

  public String getKey() {
    return key;
  }
  public String getName() {
    return name;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public boolean isDecisionTable() {
    return isDecisionTable;
  }

  public List<DecisionDto> getRequiredDecisions() {
    return requiredDecisions;
  }

  @Override
  public String toString() {
    return "DecisionDto{" +
      "name='" + name + '\'' +
      ", key='" + key + '\'' +
      ", isDecisionTable=" + isDecisionTable +
      ", requiredDecisions=" + requiredDecisions +
      '}';
  }
}
