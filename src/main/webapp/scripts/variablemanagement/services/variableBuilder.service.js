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
        annotations: {
          en: variableFromExcel['annotations.en'],
          de: variableFromExcel['annotations.de']
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
        relatedVariables: variableFromJson.relatedVariables,
        surveyIds: variableFromJson.surveyIds,
        dataSetIds: variableFromJson.dataSetIds,
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        generationDetails: {
          rule: variableFromExcel['generationDetails.rule'],
          ruleExpressionLanguage: variableFromExcel[
            'generationDetails.ruleExpressionLanguage'],
          description: {
            en: variableFromExcel['generationDetails.description.en'],
            de: variableFromExcel['generationDetails.description.de']
          }
        },
        distribution: variableFromJson.distribution,
      };
      console.log(variableObj);
      return new VariableResource(CleanJSObjectService.removeEmptyJsonObjects(
        variableObj));
    };
    return {
      buildVariable: buildVariable
    };
  });
