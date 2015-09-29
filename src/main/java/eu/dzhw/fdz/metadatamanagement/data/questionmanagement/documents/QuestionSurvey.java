package eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractNestedSurvey;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * This is the representation of a survey where a given question was asked.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders")
public class QuestionSurvey extends AbstractNestedSurvey{
  
  public QuestionSurvey() {
    super();
  }
}
