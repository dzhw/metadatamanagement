package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.QuestionManagementService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for deleting questions of a data acquisition project.
 *
 * @author tgehrke
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeleteAllQuestionsResourceController {

  private final QuestionManagementService questionService;

  /**
   * delete all questions from data acquisition project.
   *
   * @param id the Id of the project.
   * @return no Content.
   */
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER})
  @DeleteMapping(value = "/data-acquisition-projects/{id}/questions")
  public ResponseEntity<Question> delete(@PathVariable String id) {
    questionService.deleteQuestionsByProjectId(id);
    return ResponseEntity.noContent().build();
  }
}

