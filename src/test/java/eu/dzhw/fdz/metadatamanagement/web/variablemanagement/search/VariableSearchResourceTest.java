/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;

import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details.VariableResource;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableSearchResourceTest extends AbstractWebTest {

  @SuppressWarnings("unchecked")
  @Test
  public void testHashCode() throws ClassNotFoundException {

    // Arrange
    Link[] links = new Link[1];
    Link link = new Link("href");
    links[0] = link;
    
    VariableSearchPageResource variableSearchPageResource =
        new VariableSearchPageResource(null,
            (Class<VariableSearchController>) Class
                .forName(VariableSearchController.class.getName()),
            new ControllerLinkBuilderFactory(), null, null);
    VariableSearchPageResource variableSearchPageResource2 =
        new VariableSearchPageResource(new PagedResources<VariableResource>(new ArrayList<>(), null, links),
            (Class<VariableSearchController>) Class
                .forName(VariableSearchController.class.getName()),
        new ControllerLinkBuilderFactory(), null, null);


    // Act
    int withoutPageHashCode = variableSearchPageResource.hashCode();
    int withPageHashCode = variableSearchPageResource2.hashCode();



    // Assert
    assertEquals(-97370770, withoutPageHashCode);
    assertEquals(111492632, withPageHashCode);
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testEquals() throws ClassNotFoundException{
    //Arrange
    
    Link[] links = new Link[1];
    Link link = new Link("href");
    links[0] = link;
    
    Link[] links2 = new Link[1];
    Link link2 = new Link("Anotherhref");
    links2[0] = link2;
    
    VariableSearchPageResource variableSearchPageResource =
        new VariableSearchPageResource(null,
            (Class<VariableSearchController>) Class
                .forName(VariableSearchController.class.getName()),
            new ControllerLinkBuilderFactory(), null, null);
    VariableSearchPageResource variableSearchPageResource2 =
        new VariableSearchPageResource(new PagedResources<VariableResource>(new ArrayList<>(), null, links),
            (Class<VariableSearchController>) Class
                .forName(VariableSearchController.class.getName()),
        new ControllerLinkBuilderFactory(), null, null);
    VariableSearchPageResource variableSearchPageResource3 =
        new VariableSearchPageResource(new PagedResources<VariableResource>(new ArrayList<>(), null, links),
            (Class<VariableSearchController>) Class
                .forName(VariableSearchController.class.getName()),
        new ControllerLinkBuilderFactory(), null, null);
    VariableSearchPageResource variableSearchPageResource4 =
        new VariableSearchPageResource(null,
            (Class<VariableSearchController>) Class
                .forName(VariableSearchController.class.getName()),
            new ControllerLinkBuilderFactory(), null, null);
    VariableSearchPageResource variableSearchPageResource5 =
        new VariableSearchPageResource(new PagedResources<VariableResource>(new ArrayList<>(), null, links2),
            (Class<VariableSearchController>) Class
                .forName(VariableSearchController.class.getName()),
        new ControllerLinkBuilderFactory(), null, null);
    
    //Act
    boolean checkSame = variableSearchPageResource.equals(variableSearchPageResource);
    boolean checkNull = variableSearchPageResource.equals(null);
    boolean checkOtherClass = variableSearchPageResource.equals(new Object()); 
    boolean checkPageNone = variableSearchPageResource.equals(variableSearchPageResource4);
    boolean checkPageOther = variableSearchPageResource.equals(variableSearchPageResource2);
    boolean checkPageOtherBoth = variableSearchPageResource3.equals(variableSearchPageResource5);
    boolean checkPageOtherBothSame = variableSearchPageResource3.equals(variableSearchPageResource2);
    
    //Assert
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);
    assertEquals(true, checkPageNone);
    assertEquals(false, checkPageOther);
    assertEquals(false, checkPageOtherBoth);
    assertEquals(true, checkPageOtherBothSame);
  }

}
