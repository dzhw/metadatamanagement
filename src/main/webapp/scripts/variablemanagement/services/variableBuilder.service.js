'use strict';

angular.module('metadatamanagementApp').service('VariableBuilderService',
  function(VariableResource, CleanJSObjectService) {
    var buildVariable = function(variableFromExcel, variableFromJson,
      dataAcquisitionProjectId) {
      if (!variableFromExcel || !variableFromJson ||
        !dataAcquisitionProjectId) {
        return null;
      }

      var variableObj = {
        id: variableFromExcel.id,
        dataType: variableFromJson.dataType,
        scaleLevel: variableFromJson.scaleLevel,
        name: variableFromJson.name,
        label: variableFromJson.label,
        questionId: variableFromExcel.questionId,
        description: {
          en: variableFromExcel['description.en'],
          de: variableFromExcel['description.de']
        },
        accessWays: CleanJSObjectService.
        removeWhiteSpace(variableFromExcel.accessWays),
        relatedQuestionStrings: {
          en: variableFromExcel['relatedQuestionStrings.en'],
          de: variableFromExcel['relatedQuestionStrings.de']
        },
        filterDetails: {
          expression: variableFromExcel['filterDetails.expression'],
          description: {
            en: variableFromExcel['filterDetails.description.de'],
            de: variableFromExcel['filterDetails.description.en']
          },
          expressionLanguage: variableFromExcel[
            'filterDetails.expressionLanguage'
          ]
        },
        sameVariablesInPanel: variableFromJson.sameVariablesInPanel,
        surveyIds: variableFromJson.surveyIds,
        dataSetIds: variableFromJson.dataSetIds,
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        generationDetails: {
          rule: variableFromExcel['generationDetails.rule'],
          ruleExpressionLanguage: variableFromExcel[
            'generationDetails.ruleExpressionLanguage'],
          annotations: {
            en: variableFromExcel['generationDetails.annotations.en'],
            de: variableFromExcel['generationDetails.annotations.de']
          }
        },
        distribution: variableFromJson.distribution,
      };
      return new VariableResource(CleanJSObjectService.removeEmptyJsonObjects(
        variableObj));
    };
    return {
      buildVariable: buildVariable
    };
  });
