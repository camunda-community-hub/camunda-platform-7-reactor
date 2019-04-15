package org.camunda.bpm.extension.reactor.spring;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Import(CamundaReactorConfiguration.class)
@EnableConfigurationProperties(CamundaReactorConfigProperties.class)
public class CamundaReactorAutoConfiguration {

  // empty

}
