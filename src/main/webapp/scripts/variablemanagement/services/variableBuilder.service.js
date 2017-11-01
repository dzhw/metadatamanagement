/* global _ */
'use strict';

angular.module('metadatamanagementApp').service('VariableBuilderService',
  function(VariableResource, CleanJSObjectService, DataSetIdBuilderService,
    QuestionIdBuilderService, SurveyIdBuilderService, StudyIdBuilderService,
    VariableIdBuilderService, InstrumentIdBuilderService) {
    var buildVariable = function(variableFromExcel, variableFromJson, dataSet,
      variableIndex) {
      var dataAcquisitionProjectId = dataSet.dataAcquisitionProjectId;
      var variableObj = {
        id: VariableIdBuilderService.buildVariableId(
          dataAcquisitionProjectId, dataSet.dataSetNumber,
          variableFromExcel.name
        ),
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        studyId: StudyIdBuilderService.buildStudyId(
          dataAcquisitionProjectId),
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
        derivedVariablesIdentifier:
          variableFromExcel.derivedVariablesIdentifier,
        relatedVariables: CleanJSObjectService.removeWhiteSpace(
          variableFromExcel.relatedVariables),
        label: variableFromJson.label,
        dataType: variableFromJson.dataType,
        storageType: variableFromJson.storageType,
        scaleLevel: variableFromJson.scaleLevel,
        relatedQuestions: [],
        surveyNumbers: variableFromJson.surveyNumbers,
        surveyIds: [],
        indexInDataSet: variableIndex,
        distribution: variableFromJson.distribution,
        dataSetId: DataSetIdBuilderService.buildDataSetId(
          dataAcquisitionProjectId, dataSet.dataSetNumber),
        dataSetNumber: dataSet.dataSetNumber,
        doNotDisplayThousandsSeparator:
          variableFromExcel.doNotDisplayThousandsSeparator
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
