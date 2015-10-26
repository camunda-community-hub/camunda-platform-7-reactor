package org.camunda.bpm.extension.reactor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import reactor.bus.selector.Selector;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.extension.reactor.SelectorBuilder.selector;

@RunWith(Parameterized.class)
public class SelectorBuilderTest {

  @Parameters(name = "{index}: builder=''{0}'', expected=''{1}''")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {selector(), "/camunda/{process}/{element}/{event}"},
      {selector().process("foo"), "/camunda/foo/{element}/{event}"},
      {selector().process("foo").element("bar"), "/camunda/foo/bar/{event}"},
      {selector().process("foo").element("bar").event("create"), "/camunda/foo/bar/create"},
      {selector().element("bar").event("create"), "/camunda/{process}/bar/create"},
      {selector().element("bar"), "/camunda/{process}/bar/{event}"},
      {selector().event("create"), "/camunda/{process}/{element}/create"},
    });
  }

  @Parameter(0)
  public SelectorBuilder builder;

  @Parameter(1)
  public String expected;

  @Test
  public void createTopic() {
    assertThat(builder.createTopic()).isEqualTo(expected);
  }

}
