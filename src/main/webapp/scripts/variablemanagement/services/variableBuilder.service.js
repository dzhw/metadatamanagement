/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('VariableBuilderService',
  function(VariableResource, CleanJSObjectService, DataSetIdBuilderService,
    QuestionIdBuilderService, SurveyIdBuilderService, StudyIdBuilderService,
    VariableIdBuilderService, InstrumentIdBuilderService) {
    var buildVariable = function(variableFromJson, dataSet) {
      var dataAcquisitionProjectId = dataSet.dataAcquisitionProjectId;
      var variableObj = variableFromJson;
      variableObj.id = VariableIdBuilderService.buildVariableId(
        dataAcquisitionProjectId, dataSet.dataSetNumber,
        variableFromJson.name
      );
      variableObj.dataAcquisitionProjectId = dataAcquisitionProjectId;
      variableObj.studyId = StudyIdBuilderService.buildStudyId(
        dataAcquisitionProjectId);
      variableObj.dataSetId = DataSetIdBuilderService.buildDataSetId(
        dataAcquisitionProjectId, dataSet.dataSetNumber);
      variableObj.dataSetNumber = dataSet.dataSetNumber;
      variableObj.surveyIds = [];
      _.forEach(variableObj.surveyNumbers, function(number) {
        variableObj.surveyIds
          .push(SurveyIdBuilderService.buildSurveyId(
            variableObj.dataAcquisitionProjectId, number));
      });
      variableObj.relatedQuestions = [];
      _.forEach(variableFromJson.relatedQuestions, function(
        relatedQuestion) {
        variableObj.relatedQuestions
          .push({
            'instrumentNumber': relatedQuestion.instrumentNumber,
            'instrumentId': InstrumentIdBuilderService.buildInstrumentId(
              dataAcquisitionProjectId, relatedQuestion.instrumentNumber
            ),
            'questionNumber': relatedQuestion.questionNumber,
            'questionId': QuestionIdBuilderService.buildQuestionId(
              dataAcquisitionProjectId,
              relatedQuestion.instrumentNumber,
              relatedQuestion.questionNumber),
            relatedQuestionStrings: relatedQuestion.relatedQuestionStrings,
          });
      });
      return new VariableResource(CleanJSObjectService
        .removeEmptyJsonObjects(variableObj));
    };
    return {
      buildVariable: buildVariable
    };
  });
