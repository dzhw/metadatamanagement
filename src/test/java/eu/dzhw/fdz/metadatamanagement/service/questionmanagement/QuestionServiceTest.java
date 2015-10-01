/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.service.questionmanagement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.FacetedPage;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.QuestionDocument;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders.QuestionSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.web.questionmanagement.search.dto.QuestionSearchFilter;

/**
 * @author Daniel Katzberg
 *
 */
public class QuestionServiceTest extends AbstractTest {

  @Autowired
  private QuestionService questionService;

  @Test
  public void testSaveAndDelete() {
    // Arrange
    QuestionDocument questionDocument = new QuestionDocumentBuilder().withId("id").withName("name")
        .withQuestion("question")
        .withQuestionSurvey(
            new QuestionSurveyBuilder().withSurveyId("surveyid").withTitle("surveyTitle").build())
        .build();

    // Act
    QuestionDocument questionDocumentSaved = this.questionService.save(questionDocument);
    QuestionDocument questionDocumentGet = this.questionService.get("id");
    this.questionService.delete("id");
    QuestionDocument questionDocumentGetAfterDelete = this.questionService.get("id");

    // Assert
    assertThat(questionDocumentSaved, is(questionDocument));
    assertThat(questionDocumentSaved, not(nullValue()));
    assertThat(questionDocumentGet, is(questionDocument));
    assertThat(questionDocumentGet, not(nullValue()));
    assertThat(questionDocumentGet.getName(), is("name"));
    assertThat(questionDocumentGet.getQuestion(), is("question"));
    assertThat(questionDocumentGet.getQuestionSurvey().getSurveyId(), is("surveyid"));
    assertThat(questionDocumentGet.getQuestionSurvey().getTitle(), is("surveyTitle"));
    assertThat(questionDocumentGet.getQuestionVariables().size(), is(0));
    assertThat(questionDocumentGetAfterDelete, is(nullValue()));
  }
  
  @Test
  public void testSearch() {
    //Arrange
    QuestionDocument questionDocument = new QuestionDocumentBuilder().withId("id").withName("name")
        .withQuestion("question")
        .withQuestionSurvey(
            new QuestionSurveyBuilder().withSurveyId("surveyid").withTitle("surveyTitle").build())
        .build();
    QuestionSearchFilter questionSearchFilter = new QuestionSearchFilter();
    questionSearchFilter.setQuery("question");
    Pageable pageable = new PageRequest(0, 10);
    
    //Act
    this.questionService.save(questionDocument);
    FacetedPage<QuestionDocument> pageWihBuckets = this.questionService.search(questionSearchFilter, pageable);
    this.questionService.delete("id");
    
    //Assert
    assertThat(pageWihBuckets.getNumberOfElements(), is(1));
    assertThat(pageWihBuckets.getContent().get(0).getId(), is("id"));
  }

}
