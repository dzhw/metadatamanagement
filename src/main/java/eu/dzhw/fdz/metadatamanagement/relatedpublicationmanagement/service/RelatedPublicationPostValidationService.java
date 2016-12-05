package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.rest.dto.PostValidationMessageDto;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;

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
  @Inject
  private RelatedPublicationRepository relatedPublicationRepository;
//  @Inject
//  private VariableRepository variableRepository;
//
//  @Inject
//  private SurveyRepository surveyRepository;
//
//  @Inject
//  private DataSetRepository dataSetRepository;

  @Inject
  private InstrumentRepository instrumentRepository;

  @Inject
  private QuestionRepository questionRepository;
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
    List<RelatedPublication> relatedPublications = this.relatedPublicationRepository.findAll();
    
    for (RelatedPublication relatedPublication : relatedPublications) {
      
      //Validate Instruments
      errors = this.postValidateInstruments(relatedPublication, errors);
      
      //Validate Questions
      errors = this.postValidateQuestions(relatedPublication, errors);
    }
    
    return errors;
  }

  /**
   * 
   * @param questionIds A list of ids of all referenced questions.
   * @param errors The list with all recognized errors.    
   * @return The updated list of errors.
   */
  private List<PostValidationMessageDto> postValidateInstruments(
      RelatedPublication relatedPublication, List<PostValidationMessageDto> errors) {
    
    List<String> instrumentIds = relatedPublication.getInstrumentIds();
    
    //check all referenced question ids
    for (String instrumentId : instrumentIds) {
      Instrument instrument = this.instrumentRepository.findOne(instrumentId);
      
      //check for exting referenced
      if (instrument == null) {
        String[] information = {instrumentId, relatedPublication.getId()};
        errors.add(new PostValidationMessageDto("instrument-management.error."
            + "post-validation.question-unknown", Arrays.asList(information)));
      } else { //All other checks, where question is != null
        //is the same study id referenced to the related publication
        if (!relatedPublication.getStudyIds().contains(instrument.getDataAcquisitionProjectId())) {
          String[] information = {instrumentId, relatedPublication.getId(), 
              instrument.getDataAcquisitionProjectId()};
          errors.add(new PostValidationMessageDto("instrument-management.error."
              + "post-validation.question-has-not-a-referenced-study", Arrays.asList(information)));
        }
      }      
    }
    
    return errors;
  }

  /**
   * 
   * @param relatedPublication The actual related publication 
   * @param errors The list with all recognized errors.    
   * @return The updated list of errors.
   */
  private List<PostValidationMessageDto> postValidateQuestions(
      RelatedPublication relatedPublication, List<PostValidationMessageDto> errors) {
    
    List<String> questionIds = relatedPublication.getQuestionIds();
    
    //check all referenced question ids
    for (String questionId : questionIds) {
      Question question = this.questionRepository.findOne(questionId);
      
      //check for exting referenced
      if (question == null) {
        String[] information = {questionId, relatedPublication.getId()};
        errors.add(new PostValidationMessageDto("question-management.error."
            + "post-validation.question-unknown", Arrays.asList(information)));
      } else { //All other checks, where question is != null
        //is the same study id referenced to the related publication
        if (!relatedPublication.getStudyIds().contains(question.getDataAcquisitionProjectId())) {
          String[] information = {questionId, relatedPublication.getId(), 
              question.getDataAcquisitionProjectId()};
          errors.add(new PostValidationMessageDto("question-management.error."
              + "post-validation.question-has-not-a-referenced-study", Arrays.asList(information)));
        }
      }      
    }
    
    return errors;
  }

}
