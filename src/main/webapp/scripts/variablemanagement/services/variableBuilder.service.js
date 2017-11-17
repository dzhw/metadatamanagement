/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('VariableBuilderService',
  function(VariableResource, CleanJSObjectService, DataSetIdBuilderService,
    QuestionIdBuilderService, SurveyIdBuilderService, StudyIdBuilderService,
    VariableIdBuilderService, InstrumentIdBuilderService) {
    function decimalPlaces(num) {
      var match = ('' + num).match(/(?:\.(\d+))?(?:[eE]([+-]?\d+))?$/);
      if (!match) { return 0; }
      return Math.max(
       0,
       // Number of digits right of decimal point.
       (match[1] ? match[1].length : 0) -
       // Adjust for scientific notation.
       (match[2] ? +match[2] : 0));
    }
    var buildVariable = function(variableFromJson, dataSet) {
      var dataAcquisitionProjectId = dataSet.dataAcquisitionProjectId;
      var variableObj = variableFromJson;
      var relatedQuestions = variableFromJson.relatedQuestions;
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
      _.forEach(relatedQuestions, function(
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
      if (variableObj.dataType.en === 'numeric' && variableObj.distribution) {
        variableObj.distribution.maxNumberOfDecimalPlaces = 0;
        if (variableObj.distribution.validResponses &&
          variableObj.distribution.validResponses.length > 0) {
          variableObj.distribution.maxNumberOfDecimalPlaces =
            decimalPlaces(_.maxBy(variableObj.distribution.validResponses,
              function(validResponse) {
                return decimalPlaces(validResponse.value);
              }).value);
        }
      }
      return new VariableResource(CleanJSObjectService
        .removeEmptyJsonObjects(variableObj));
    };
    return {
      buildVariable: buildVariable
    };
  });
