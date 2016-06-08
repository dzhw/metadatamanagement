'use strict';

angular.module('metadatamanagementApp').service('VariableBuilderService',
  function(VariableResource, CleanJSObjectService, $translate) {
    var parseErrors = [];
    var getVariables = function(variables, zip, dataAcquisitionProjectId) {
      var variablesObjArray = [];
      for (var i = 0; i < variables.length; i++) {
        var generatedVariable = {};
        try {
          generatedVariable = zip.files['variables/' + variables[i].id +
              '.json'] ?
            JSON.
          parse(zip.files['variables/' + variables[i].id + '.json'].asText()):
            undefined;
        } catch (e) {
          parseErrors.push('\n' + $translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.detail.' +
            'logMessages.malformedJsonFile', {
              id: variables[i].id
            }));
          continue;
        }
        var variableObj = {
          id: variables[i].id,
          dataType: generatedVariable ? generatedVariable.dataType : undefined,
          scaleLevel: generatedVariable ?
            generatedVariable.scaleLevel : undefined,
          name: generatedVariable ? generatedVariable.name : undefined,
          label: generatedVariable ? generatedVariable.label : undefined,
          description: {
            en: variables[i]['description.en'],
            de: variables[i]['description.de']
          },
          accessWays: CleanJSObjectService.
          removeWhiteSpace(variables[i].accessWays),

          filterDetails: {
            expression: variables[i]['filterDetails.expression'],
            description: {
              en: variables[i]['filterDetails.description.de'],
              de: variables[i]['filterDetails.description.en']
            },
            expressionLanguage: variables[i][
              'filterDetails.expressionLanguage'
            ]
          },
          sameVariablesInPanel: generatedVariable ?
            generatedVariable.sameVariablesInPanel : undefined,
          surveyIds: generatedVariable ?
            generatedVariable.surveyIds : undefined,
          conceptId: variables[i].conceptId,
          dataSetIds: generatedVariable ?
            generatedVariable.dataSetIds : undefined,
          atomicQuestionId: generatedVariable ?
            generatedVariable.atomicQuestionId : undefined,
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
          distribution: generatedVariable ?
            generatedVariable.distribution : undefined
        };
        CleanJSObjectService.removeEmptyJsonObjects(variableObj);
        variablesObjArray.push(new VariableResource(variableObj));
      }
      return variablesObjArray;
    };
    return {
      getVariables: getVariables,
      getParseErrors: parseErrors
    };
  });
