package org.camunda.bpm.extension.reactor.event;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.extension.reactor.projectreactor.bus.Event;

/**
 * @param <T> the type of the event value (configuration or engine)
 * @param <S> the type of the event itself, used for notify by type.
 */
public abstract class ProcessEnginePluginEvent<T, S> extends Event<T> {

  /**
   * Fired on {@link org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin#preInit(ProcessEngineConfigurationImpl)}
   *
   * @param processEngineConfiguration the configuration pre-init
   * @return new event
   */
  public static PreInitEvent preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
    return new PreInitEvent(processEngineConfiguration);
  }

  /**
   * Fired on {@link org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin#postInit(ProcessEngineConfigurationImpl)}
   *
   * @param processEngineConfiguration the configuration post-init
   * @return new event
   */
  public static PostInitEvent postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
    return new PostInitEvent(processEngineConfiguration);
  }

  /**
   * Fired on {@link org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin#postProcessEngineBuild(ProcessEngine)}
   *
   * @param processEngine the newly built process engine
   * @return new event
   */
  public static PostProcessEngineBuild postProcessEngineBuild(ProcessEngine processEngine) {
    return new PostProcessEngineBuild(processEngine);
  }


  private final Class<S> type;

  protected ProcessEnginePluginEvent(T data, Class<S> type) {
    super(data);
    this.type = type;
  }

  public static class PreInitEvent extends ProcessEnginePluginEvent<ProcessEngineConfigurationImpl, PreInitEvent> {

    protected PreInitEvent(final ProcessEngineConfigurationImpl processEngineConfiguration) {
      super(processEngineConfiguration, PreInitEvent.class);
    }
  }

  public static class PostInitEvent extends ProcessEnginePluginEvent<ProcessEngineConfigurationImpl, PostInitEvent> {

    protected PostInitEvent(final ProcessEngineConfigurationImpl processEngineConfiguration) {
      super(processEngineConfiguration, PostInitEvent.class);
    }
  }

  public static class PostProcessEngineBuild extends ProcessEnginePluginEvent<ProcessEngine, PostProcessEngineBuild> {
    protected PostProcessEngineBuild(final ProcessEngine processEngine) {
      super(processEngine, PostProcessEngineBuild.class);
    }
  }

  public Class<S> getType() {
    return type;
  }
}
