'use strict';

angular.module('metadatamanagementApp').service('VariableBuilderService',
function(VariableResource, CleanJSObjectService) {
  var getVariables = function(variables, zip, dataAcquisitionProjectId) {
    var variablesObjArray = [];
    for (var i = 0; i < variables.length; i++) {
      var variableObj = {
        id: variables[i].id,
        dataType: {
          en: variables[i]['dataType.en'],
          de: variables[i]['dataType.de']
        },
        scaleLevel: {
          en: variables[i]['scaleLevel.en'],
          de: variables[i]['scaleLevel.de']
        },
        name: variables[i].name,
        label: {
          en: variables[i]['label.en'],
          de: variables[i]['label.de']
        },
        description: {
          en: variables[i]['description.en'],
          de: variables[i]['description.de']
        },
        accessWays: CleanJSObjectService.
        removeWhiteSpace(variables[i].accessWays),

        filterDetails: {
          expression:
          variables[i]['filterDetails.expression'],
          description: {
            en: variables[i]['filterDetails.description.de'],
            de: variables[i]['filterDetails.description.en']
          },
          expressionLanguage:
          variables[i]['filterDetails.expressionLanguage']
        },
        sameVariablesInPanel: CleanJSObjectService.
        removeWhiteSpace(variables[i].sameVariablesInPanel),
        surveyIds: CleanJSObjectService.
        removeWhiteSpace(variables[i].surveyIds),
        conceptId: variables[i].conceptId,
        dataSetIds: CleanJSObjectService.
        removeWhiteSpace(variables[i].dataSetIds),
        atomicQuestionId: variables[i].atomicQuestionId,
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        generationDetails: {
          rule: variables[i]['generationDetails.rule'],
          ruleExpressionLanguage: variables[i]
          ['generationDetails.ruleExpressionLanguage'],
          description: {
            en: variables[i]['generationDetails.description.en'],
            de: variables[i]['generationDetails.description.de']
          }
        },
        distribution: zip.files['values/' + variables[i].id + '.json'] ?
          JSON.parse(zip.files['values/' + variables[i].id + '.json']
          .asBinary()).distribution : undefined,
      };
      CleanJSObjectService.removeEmptyJsonObjects(variableObj);
      variablesObjArray.push(new VariableResource(variableObj));
    }
    return variablesObjArray;
  };
  return {
      getVariables: getVariables
    };
});
