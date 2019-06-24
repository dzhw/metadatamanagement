package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Tracks a {@link Question} before and after saving it through the repository
 * and provides access to some changes.
 */
@Component
@RequestScope
public class QuestionChangesProvider {

  private Map<String, Question> oldQuestions = new HashMap<>();
  private Map<String, Question> newQuestions = new HashMap<>();

  /**
   * Gets the removed concept ids of a question.
   * @param questionId Question id
   * @return Removed concept ids
   */
  public Set<String> getRemovedConceptIds(String questionId) {
    Question oldQuestion = oldQuestions.get(questionId);

    if (oldQuestion == null) {
      return Collections.emptySet();
    }

    Question newQuestion = newQuestions.get(questionId);

    if (newQuestion == null) {
      return Collections.emptySet();
    }

    List<String> oldConceptIds = oldQuestion.getConceptIds();

    if (oldConceptIds == null || oldConceptIds.isEmpty()) {
      return Collections.emptySet();
    }

    List<String> newConceptIds = newQuestion.getConceptIds();

    if (newConceptIds == null || newConceptIds.isEmpty()) {
      return Collections.emptySet();
    }

    return oldConceptIds.stream().filter(conceptId -> !newConceptIds.contains(conceptId))
        .collect(Collectors.toSet());
  }

  protected void put(Question oldQuestion, Question newQuestion) {
    if (oldQuestion != null) {
      oldQuestions.put(oldQuestion.getId(), oldQuestion);
    }

    if (newQuestion != null) {
      newQuestions.put(newQuestion.getId(), newQuestion);
    }
  }
}
