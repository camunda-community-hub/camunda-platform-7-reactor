package org.camunda.bpm.extension.reactor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.extension.reactor.SelectorBuilder.selector;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SelectorBuilderTest {

  @Parameters(name = "{index}: builder=''{0}'', expected=''{1}''")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {selector(), "/camunda/{queue}/{type}/{process}/{element}/{event}"},
      {selector().process("foo"), "/camunda/{queue}/{type}/foo/{element}/{event}"},
      {selector().process("foo").element("bar"), "/camunda/{queue}/{type}/foo/bar/{event}"},
      {selector().process("foo").element("bar").event("create"), "/camunda/{queue}/{type}/foo/bar/create"},
      {selector().element("bar").event("create"), "/camunda/{queue}/{type}/{process}/bar/create"},
      {selector().element("bar"), "/camunda/{queue}/{type}/{process}/bar/{event}"},
      {selector().event("create"), "/camunda/{queue}/{type}/{process}/{element}/create"},
      {selector().type("type"), "/camunda/{queue}/type/{process}/{element}/{event}"},
      {selector().type(""), "/camunda/{queue}/{type}/{process}/{element}/{event}"},
      {selector().caseDefinitionKey("foo"), "/camunda/{queue}/{type}/foo/{element}/{event}"},
      {selector().caseDefinitionKey("foo").element("bar"), "/camunda/{queue}/{type}/foo/bar/{event}"},
      {selector().caseDefinitionKey("foo").element("bar").event("create"), "/camunda/{queue}/{type}/foo/bar/create"},
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
