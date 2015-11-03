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
      {selector(), "/camunda/{type}/{process}/{element}/{event}"},
      {selector().process("foo"), "/camunda/{type}/foo/{element}/{event}"},
      {selector().process("foo").element("bar"), "/camunda/{type}/foo/bar/{event}"},
      {selector().process("foo").element("bar").event("create"), "/camunda/{type}/foo/bar/create"},
      {selector().element("bar").event("create"), "/camunda/{type}/{process}/bar/create"},
      {selector().element("bar"), "/camunda/{type}/{process}/bar/{event}"},
      {selector().event("create"), "/camunda/{type}/{process}/{element}/create"},
      {selector().type("type"), "/camunda/type/{process}/{element}/{event}"},
      {selector().type(""), "/camunda/{type}/{process}/{element}/{event}"},
    });
  }

  @Parameter(0)
  public SelectorBuilder builder;

  @Parameter(1)
  public String expected;

  @Test
  public void createTopic() {
    assertThat(builder.key()).isEqualTo(expected);
  }

}
