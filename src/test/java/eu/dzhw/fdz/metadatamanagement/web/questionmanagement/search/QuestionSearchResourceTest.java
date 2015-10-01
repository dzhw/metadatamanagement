/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.questionmanagement.search;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.web.questionmanagement.details.QuestionResource;

/**
 * @author Daniel Katzberg
 *
 */
public class QuestionSearchResourceTest extends AbstractTest {
  
  @SuppressWarnings("unchecked")
  @Test
  public void testHashCode() throws ClassNotFoundException {

    // Arrange
    Link[] links = new Link[1];
    Link link = new Link("href");
    links[0] = link;

    QuestionSearchResource questionSearchResource =
        new QuestionSearchResource(null,
            (Class<QuestionSearchController>) Class
                .forName(QuestionSearchController.class.getName()),
            new ControllerLinkBuilderFactory(), null, null);
    QuestionSearchResource questionSearchResource2 =
        new QuestionSearchResource(
            new PagedResources<QuestionResource>(new ArrayList<>(), null, links),
            (Class<QuestionSearchController>) Class
                .forName(QuestionSearchController.class.getName()),
        new ControllerLinkBuilderFactory(), null, null);

    // Act
    int withoutPageHashCode = questionSearchResource.hashCode();
    int withPageHashCode = questionSearchResource2.hashCode();

    // Assert
    assertThat(withoutPageHashCode, not(0));
    assertThat(withPageHashCode, not(0));
    assertThat(withoutPageHashCode, not(withPageHashCode));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testEquals() throws ClassNotFoundException {
    // Arrange

    Link[] links = new Link[1];
    Link link = new Link("href");
    links[0] = link;

    QuestionSearchResource questionSearchResource =
        new QuestionSearchResource(null,
            (Class<QuestionSearchController>) Class
                .forName(QuestionSearchController.class.getName()),
            new ControllerLinkBuilderFactory(), null, null);
    QuestionSearchResource questionSearchResource2 =
        new QuestionSearchResource(
            new PagedResources<QuestionResource>(new ArrayList<>(), null, links),
            (Class<QuestionSearchController>) Class
                .forName(QuestionSearchController.class.getName()),
        new ControllerLinkBuilderFactory(), null, null);
    QuestionSearchResource questionSearchResource3 =
        new QuestionSearchResource(
            new PagedResources<QuestionResource>(new ArrayList<>(), null, links),
            (Class<QuestionSearchController>) Class
                .forName(QuestionSearchController.class.getName()),
        new ControllerLinkBuilderFactory(), null, null);


    // Act
    boolean checkSame = questionSearchResource.equals(questionSearchResource);
    boolean checkNull = questionSearchResource.equals(null);
    boolean checkOtherClass = questionSearchResource.equals(new Object());
    boolean checkPageOther = questionSearchResource.equals(questionSearchResource2);
    boolean checkPageOtherBothSame =
        questionSearchResource3.equals(questionSearchResource2);

    // Assert
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);
    assertEquals(false, checkPageOther);
    assertEquals(true, checkPageOtherBothSame);
  }
}
