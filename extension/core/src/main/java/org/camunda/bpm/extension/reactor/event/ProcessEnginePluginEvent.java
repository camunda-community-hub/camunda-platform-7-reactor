package org.camunda.bpm.extension.reactor.event;


import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import reactor.bus.Event;
import reactor.bus.EventBus;

/**
 * @param <T> the type on the event value (configuration or engine)
 * @param <S> the type on the event itself, used for notify by type.
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

  public Class<S> getType() {
    return type;
  }

  public void notify(final EventBus eventBus) {
    eventBus.notify(getType(), this);
  }
}
