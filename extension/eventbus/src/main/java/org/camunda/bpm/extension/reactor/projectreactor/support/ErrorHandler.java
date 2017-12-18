package org.camunda.bpm.extension.reactor.projectreactor.support;

import java.util.function.Consumer;


public interface ErrorHandler  extends Consumer<Throwable> {

  ErrorHandler THROW_RUNTIME_EXCEPTION = e -> {throw new RuntimeException(e); };

}
