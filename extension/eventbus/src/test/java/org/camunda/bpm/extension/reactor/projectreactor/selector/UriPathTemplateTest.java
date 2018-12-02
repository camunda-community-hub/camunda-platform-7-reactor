package org.camunda.bpm.extension.reactor.projectreactor.selector;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Tobias Behr
 */
public class UriPathTemplateTest {

  private final UriPathTemplate template = new UriPathTemplate("a*b*a*b");

  @Test
  public void should_be_save_for_multi_threading() throws Exception {
    final Consumer<Integer> r = a -> {
      try {
        template.matches("aaabbaabaabaaabaaaabaabaaaaabbaaabbab");
      } catch (StringIndexOutOfBoundsException e) {
        e.printStackTrace();
        throw e;
      }
    };
    final Runnable s = () -> Stream.iterate(0, i -> i + 1).limit(100000).forEach(r);

    final List<Thread> threads = Stream.iterate(0, i -> i + 1).limit(10).map(i -> new Thread(s)).collect(Collectors.toList());
    threads.forEach(t -> t.setUncaughtExceptionHandler((t1, e) -> t1.getThreadGroup().interrupt()));
    threads.forEach(Thread::start);
    threads.forEach(t -> {
      try {
        t.join();
      } catch (InterruptedException ire) {
        Assert.fail();
      } catch (Exception e) {
        throw e;
      }
    });
  }
}
