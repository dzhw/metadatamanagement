package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.rest.dto.PostValidationMessageDto;

/**
 * This service handels the post-validation of related publications. It checks the foreign keys and 
 * references between to other domain objects. If a foreign key or reference is not valid, 
 * the service adds an error message to a list. Finally the service returns a list with all errors.
 *
 * @author Daniel Katzberg
 *
 */
@Service
public class RelatedPublicationPostValidationService {

  /* Repositories for loading data from the repository */
//  @Inject
//  private VariableRepository variableRepository;
//
//  @Inject
//  private SurveyRepository surveyRepository;
//
//  @Inject
//  private DataSetRepository dataSetRepository;
//
//  @Inject
//  private InstrumentRepository instrumentRepository;
//
//  @Inject
//  private QuestionRepository questionRepository;
//  
//  @Inject
//  private StudyRepository studyRepository;

  /**
   * This method handels the complete post validation of all relatedPublications.
   * 
   * @return a list of all post validation errors.
   */
  public List<PostValidationMessageDto> postValidate() {

    List<PostValidationMessageDto> errors = new ArrayList<>();

    //TODO DKatzberg Implement the post validation for the related publications. 
    
    return errors;
  }

}
