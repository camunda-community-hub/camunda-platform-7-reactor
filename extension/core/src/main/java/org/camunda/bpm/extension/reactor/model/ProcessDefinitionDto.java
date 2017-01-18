package org.camunda.bpm.extension.reactor.model;

import java.io.Serializable;

import org.camunda.bpm.engine.repository.ProcessDefinition;

public class ProcessDefinitionDto extends AbstractDto {

  private static final long serialVersionUID = 1L;

  public static ProcessDefinitionDto from(ProcessDefinition definition) {
    final ProcessDefinitionDto dto = new ProcessDefinitionDto();
    dto.id = definition.getId();
    dto.key = definition.getKey();
    dto.category = definition.getCategory();
    dto.description = definition.getDescription();
    dto.name = definition.getName();
    dto.version = definition.getVersion();
    dto.resource = definition.getResourceName();
    dto.deploymentId = definition.getDeploymentId();
    dto.diagram = definition.getDiagramResourceName();
    dto.suspended = definition.isSuspended();
    dto.tenantId = definition.getTenantId();
    dto.versionTag = definition.getVersionTag();

    return dto;
  }

  protected String category;
  protected String description;
  protected int version;
  protected String resource;
  protected String deploymentId;
  protected String diagram;
  protected boolean suspended;
  protected String tenantId;
  protected String versionTag;

  public String getKey() {
    return key;
  }

  public String getCategory() {
    return category;
  }

  public String getDescription() {
    return description;
  }

  public String getName() {
    return name;
  }

  public int getVersion() {
    return version;
  }

  public String getResource() {
    return resource;
  }

  public String getDeploymentId() {
    return deploymentId;
  }

  public String getDiagram() {
    return diagram;
  }

  public boolean isSuspended() {
    return suspended;
  }

  public String getTenantId() {
    return tenantId;
  }

  public String getVersionTag() {
    return versionTag;
  }

}

