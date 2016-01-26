package org.camunda.bpm.extension.reactor.bus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.extension.reactor.bus.SelectorBuilder.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder.*;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Runs multiple test contexts with enlosed runner to separate concerns.
 */
@RunWith(Enclosed.class)
public class SelectorBuilderTest {

  /**
   * Test multiple value combinations.
   */
  @RunWith(Parameterized.class)
  public static class FromValues {

    @Parameters(name = "{index}: builder=''{0}'', expected=''{1}''")
    public static Collection<Object[]> data() {
      return Arrays.asList(new Object[][]{
        {selector(), "/camunda/{context}/{type}/{process}/{element}/{event}"},
        {selector().process("foo"), "/camunda/{context}/{type}/foo/{element}/{event}"},
        {selector().process("foo").element("bar"), "/camunda/{context}/{type}/foo/bar/{event}"},
        {selector().process("foo").element("bar").event("create"), "/camunda/{context}/{type}/foo/bar/create"},
        {selector().element("bar").event("create"), "/camunda/{context}/{type}/{process}/bar/create"},
        {selector().element("bar"), "/camunda/{context}/{type}/{process}/bar/{event}"},
        {selector().event("create"), "/camunda/{context}/{type}/{process}/{element}/create"},
        {selector().type("type"), "/camunda/{context}/type/{process}/{element}/{event}"},
        {selector().type(""), "/camunda/{context}/{type}/{process}/{element}/{event}"},
        {selector().caseDefinitionKey("foo"), "/camunda/{context}/{type}/foo/{element}/{event}"},
        {selector().caseDefinitionKey("foo").element("bar"), "/camunda/{context}/{type}/foo/bar/{event}"},
        {selector().caseDefinitionKey("foo").element("bar").event("create"), "/camunda/{context}/{type}/foo/bar/create"},
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

  /**
   * Test extract values from annotation.
   */
  public static class FromAnnotation {

    @CamundaSelector
    public static class AnnotationOnClass {
    }

    public static class AnnotationOnParent extends AnnotationOnClass {
    }

    public static class AnnotationMissing {
    }

    @CamundaSelector(element = "e", event = "ev", process = "p", type = "t", context = Context.cmmn)
    public static abstract class Full {
    }

    @CamundaSelector
    public static class Empty {
    }


    @Test
    public void gets_annotation_if_present() {
      assertThat(fromCamundaSelector(AnnotationOnClass.class)).isNotNull();
    }

    @Test
    public void gets_annotation_if_present_on_parent() {
      assertThat(fromCamundaSelector(AnnotationOnParent.class)).isNotNull();
    }

    @Test(expected = IllegalStateException.class)
    public void fails_if_annotation_not_present_in_hierarchy() {
      fromCamundaSelector(AnnotationMissing.class);
    }

    @Test
    public void full_selector_path() {
      assertThat(fromCamundaSelector(Full.class).key()).isEqualTo("/camunda/cmmn/t/p/e/ev");
    }

    @Test
    public void empty_selector_path() {
      assertThat(SelectorBuilder.fromCamundaSelector(Empty.class).key()).isEqualTo("/camunda/bpmn/{type}/{process}/{element}/{event}");
    }

  }

  public static class GetDefinitionKey {


    @Test
    public void retrieve_processDefinitionKey_from_definitionId() {
      assertThat(SelectorBuilder.defintionKey("process_a:1:3")).isEqualTo("process_a");
    }

    @Test
    public void retrieve_caseDefinitionKey_from_definitionId() {
      assertThat(caseDefintionKey("case_a:1:3")).isEqualTo("case_a");
    }
  }

  public static class NotificationKey {

    @Test
    public void creates_key_for_task() {
      final DelegateTask task = mock(DelegateTask.class, RETURNS_DEEP_STUBS);

      when(task.getBpmnModelElementInstance().getElementType().getTypeName()).thenReturn("userTask");
      when(task.getProcessDefinitionId()).thenReturn("process:1:1");
      when(task.getEventName()).thenReturn("create");
      when(task.getTaskDefinitionKey()).thenReturn("task1");

      assertThat(SelectorBuilder.selector(task).key()).isEqualTo("/camunda/task/{type}/process/task1/create");
    }
  }
}
