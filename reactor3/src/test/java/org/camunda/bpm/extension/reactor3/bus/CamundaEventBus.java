package org.camunda.bpm.extension.reactor3.bus;



import org.junit.Test;
import reactor.core.publisher.TopicProcessor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CamundaEventBus {

  private static class LoggingSubscriber<T> extends UnboundedSubscriber<T> {

    @Override
    public void hookOnNext(T event) {
      System.out.println(String.format("[%s] received: %s", Thread.currentThread().getName(), event));
    }
  }


  @Test
  public void name1() throws Exception {
    CountDownLatch c = new CountDownLatch(3);

    System.out.println("thread: " + Thread.currentThread().getName());



    TopicProcessor<String> tp = TopicProcessor.create(false);

    tp.subscribe(new LoggingSubscriber<>());

    c.await(1000, TimeUnit.SECONDS);

    tp.onNext("foo");

  }
}
