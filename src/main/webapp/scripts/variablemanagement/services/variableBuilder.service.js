/* global XLSX*/
'use strict';

angular.module('metadatamanagementApp').service('VariableBuilderService',
function(Variable, CleanJSObjectService, JobLoggingService) {
  var getVariables = function(data, dataAcquisitionProjectId) {
    var jsonContent = {};
    var variablesObjArray = [];
    try {
      var excelFile = data.files['variables.xlsx'];
      var content = XLSX.read(excelFile.asBinary(), {type: 'binary'});
      var sheetList = content.SheetNames;
      var worksheet = content.Sheets[sheetList[0]];
      // jscs:disable
      jsonContent = XLSX.utils.sheet_to_json(worksheet);
      // jscs:enable
      for (var i = 0; i < jsonContent.length; i++) {
        var variableObj = {
        id: jsonContent[i].id,
        dataType: {
          en: jsonContent[i]['dataType.en'],
          de: jsonContent[i]['dataType.de']
        },
        scaleLevel: {
          en: jsonContent[i]['scaleLevel.en'],
          de: jsonContent[i]['scaleLevel.de']
        },
        name: jsonContent[i].name,
        label: {
          en: jsonContent[i]['label.en'],
          de: jsonContent[i]['label.de']
        },
        description: {
          en: jsonContent[i]['description.en'],
          de: jsonContent[i]['description.de']
        },
        accessWays: CleanJSObjectService.
        removeWhiteSpace(jsonContent[i].accessWays),

        filterDetails: {
          expression:
          jsonContent[i]['filterDetails.expression'],
          description: {
            en: jsonContent[i]['filterDetails.description.de'],
            de: jsonContent[i]['filterDetails.description.en']
          },
          expressionLanguage:
          jsonContent[i]['filterDetails.expressionLanguage']
        },
        sameVariablesInPanel: CleanJSObjectService.
        removeWhiteSpace(jsonContent[i].sameVariablesInPanel),
        surveyIds: CleanJSObjectService.
        removeWhiteSpace(jsonContent[i].surveyIds),
        conceptId: jsonContent[i].conceptId,
        dataSetIds: CleanJSObjectService.
        removeWhiteSpace(jsonContent[i].dataSetIds),
        atomicQuestionId: jsonContent[i].atomicQuestionId,
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        generationDetails: {
          rule: jsonContent[i]['generationDetails.rule'],
          ruleExpressionLanguage: jsonContent[i]
          ['generationDetails.ruleExpressionLanguage'],
          description: {
            en: jsonContent[i]['generationDetails.description.en'],
            de: jsonContent[i]['generationDetails.description.de']
          }
        },
        distribution: data.files['values/' + jsonContent[i].id + '.json'] ?
          JSON.parse(data.files['values/' + jsonContent[i].id + '.json']
          .asBinary()).distribution : undefined,
      };
        CleanJSObjectService.removeEmptyJsonObjects(variableObj);
        variablesObjArray.push(new Variable(variableObj));
      }
    }catch (e) {
      console.log(e);
      JobLoggingService.cancel('' + e, {});
    }
    return variablesObjArray;
  };
  return {
      getVariables: getVariables
    };
});
