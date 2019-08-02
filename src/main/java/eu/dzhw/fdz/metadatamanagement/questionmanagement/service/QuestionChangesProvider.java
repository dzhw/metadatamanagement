package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import eu.dzhw.fdz.metadatamanagement.common.service.DomainObjectChangesProvider;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;

/**
 * Tracks a {@link Question} before and after saving it through the repository and provides access
 * to some changes.
 */
@Component
@RequestScope
public class QuestionChangesProvider extends DomainObjectChangesProvider<Question> {
  /**
   * Gets the removed concept ids of a question.
   * 
   * @param questionId Question id
   * @return Removed concept ids
   */
  public Set<String> getRemovedConceptIds(String questionId) {
    Question oldQuestion = oldDomainObjects.get(questionId);

    if (oldQuestion == null) {
      return Collections.emptySet();
    }

    Question newQuestion = newDomainObjects.get(questionId);

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
}
