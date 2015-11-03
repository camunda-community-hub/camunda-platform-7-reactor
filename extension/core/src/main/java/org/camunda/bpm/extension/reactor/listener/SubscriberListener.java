package org.camunda.bpm.extension.reactor.listener;

import static org.slf4j.LoggerFactory.getLogger;

import org.camunda.bpm.extension.reactor.CamundaSelector;
import org.camunda.bpm.extension.reactor.SelectorBuilder;
import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.slf4j.Logger;

import reactor.bus.EventBus;
import reactor.bus.selector.Selector;
import reactor.fn.Consumer;

public abstract class SubscriberListener<T extends DelegateEvent<?>> implements Consumer<T> {

  protected final Logger logger = getLogger(this.getClass());

  public void register(final EventBus eventBus, final SelectorBuilder selectorBuilder) {
    register(eventBus, selectorBuilder.build());
  }

  @SuppressWarnings("rawtypes")
  public void register(final EventBus eventBus, final Selector selector) {
    eventBus.on(selector, this);
  }

  public void register(final EventBus eventBus) {
    final CamundaSelector annotation = this.getClass().getAnnotation(CamundaSelector.class);
    if (annotation == null) {
      throw new IllegalStateException(String.format("Unable to get @CamundaSelector annotation from %s.", this.getClass().getName()));
    }

    register(eventBus, SelectorBuilder.selector(annotation));
  }
}
