/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('VariableBuilderService',
  function(VariableResource, CleanJSObjectService, DataSetIdBuilderService,
    QuestionIdBuilderService, SurveyIdBuilderService,
    VariableIdBuilderService, InstrumentIdBuilderService) {
    var buildVariable = function(variableFromExcel, variableFromJson, dataSet) {
      var dataAcquisitionProjectId = dataSet.dataAcquisitionProjectId;
      var variableObj = {
        id: VariableIdBuilderService.buildVariableId(
          dataAcquisitionProjectId, dataSet.dataSetName,
          variableFromExcel
          .name
        ),
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        name: variableFromExcel.name,
        annotations: {
          en: variableFromExcel['annotations.en'],
          de: variableFromExcel['annotations.de']
        },
        accessWays: CleanJSObjectService.
        removeWhiteSpace(variableFromExcel.accessWays),
        filterDetails: {
          expression: variableFromExcel['filterDetails.expression'],
          description: {
            de: variableFromExcel['filterDetails.description.de'],
            en: variableFromExcel['filterDetails.description.en']
          },
          expressionLanguage: variableFromExcel[
            'filterDetails.expressionLanguage'
          ]
        },
        generationDetails: {
          rule: variableFromExcel['generationDetails.rule'],
          ruleExpressionLanguage: variableFromExcel[
            'generationDetails.ruleExpressionLanguage'],
          description: {
            en: variableFromExcel['generationDetails.description.en'],
            de: variableFromExcel['generationDetails.description.de']
          }
        },
        panelIdentifier: variableFromExcel.panelIdentifier,
        label: variableFromJson.label,
        dataType: variableFromJson.dataType,
        scaleLevel: variableFromJson.scaleLevel,
        relatedQuestions: [],
        surveyNumbers: variableFromJson.surveyNumbers,
        surveyIds: [],
        indexInDataSet: variableFromJson.indexInDataSet,
        relatedVariables: variableFromJson.relatedVariables,
        distribution: variableFromJson.distribution,
        dataSetId: DataSetIdBuilderService.buildDataSetId(
          dataAcquisitionProjectId, _.split(dataSet.dataSetName, 'ds')[
            1]),
        dataSetNumber: _.split(dataSet.dataSetName, 'ds')[1]
      };
      _.forEach(variableObj.surveyNumbers, function(number) {
        variableObj.surveyIds
          .push(SurveyIdBuilderService.buildSurveyId(
            variableObj.dataAcquisitionProjectId, number));
      });
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
