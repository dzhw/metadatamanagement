/* global XLSX*/
'use strict';

angular.module('metadatamanagementApp').service('VariablesParser',
function(Variable, ArrayParser) {
  var getVariables = function(data, dataAcquisitionProjectId) {
    var jsonContent = {};
    var variablesValues = {};
    var variablesObjArray = [];
    Object.keys(data.files).forEach(function(key) {
      var file = data.files[key];
      if (!file.dir) {
        if (file.name.indexOf('.json') > -1) {
          var jsonKey = file.name.replace('values/','').replace('.json','');
          variablesValues[jsonKey] = JSON.parse(file.asBinary());
        }
        if (file.name.indexOf('.xlsx') > -1) {
          var content = XLSX.read(file.asBinary(), {type: 'binary'});
          var sheetList = content.SheetNames;
          var worksheet = content.Sheets[sheetList[0]];
          // jscs:disable
          jsonContent = XLSX.utils.sheet_to_json(worksheet);
          // jscs:enable
          for (var i = 0; i < jsonContent.length; i++) {
            var variableObj = {
              id: jsonContent[i].id,
              accessWays: ArrayParser.getParsedArray(jsonContent[i].accessWays),
              dataAcquisitionProjectId: dataAcquisitionProjectId,
              dataType: {
                en: jsonContent[i]['dataType.en'],
                de: jsonContent[i]['dataType.de']
              },
              label: {
                en: jsonContent[i]['label.en'],
                de: jsonContent[i]['label.de']
              },
              description: {
                en: jsonContent[i]['description.en'],
                de: jsonContent[i]['description.de']
              },
              filterDetaills: {
                filterExpression:
                jsonContent[i]['filterDetaills.filterExpression'],
                filterDescription: {
                  en: jsonContent[i]['filterDetaills.filterDescription.de'],
                  de: jsonContent[i]['filterDetaills.filterDescription.en']
                },
                filterExpressionLanguage:
                jsonContent[i]['filterDetaills.filterExpressionLanguage']
              },
              sameVariablesInPanel: ArrayParser.
              getParsedArray(jsonContent[i].sameVariablesInPanel),
              generationDetails: {
                rule: jsonContent[i]['generationDetails.rule'],
                ruleExpressionLanguage: jsonContent[i]
                ['generationDetails.ruleExpressionLanguage'],
                description: {
                  en: jsonContent[i]['generationDetails.description.en'],
                  de: jsonContent[i]['generationDetails.description.de']
                }
              },
              name: jsonContent[i].name,
              scaleLevel: {
                en: jsonContent[i]['scaleLevel.en'],
                de: jsonContent[i]['scaleLevel.de']
              },
              statistics: {
                firstQuartile: jsonContent[i]['statistics.firstQuartile'],
                highWhisker: jsonContent[i]['statistics.  highWhisker'],
                kurtosis: jsonContent[i]['statistics.kurtosis'],
                lowWhisker: jsonContent[i]['statistics.lowWhisker'],
                maximum: jsonContent[i]['statistics.maximum'],
                meanValue: jsonContent[i]['statistics.meanValue'],
                median: jsonContent[i]['statistics.median'],
                minimum: jsonContent[i]['statistics.minimum'],
                skewness: jsonContent[i]['statistics.skewness'],
                standardDeviation: jsonContent[i]
                ['statistics.standardDeviation'],
                thirdQuartile: jsonContent[i]['statistics.thirdQuartile']
              },
              values: variablesValues[jsonContent[i].id] ?
                variablesValues[jsonContent[i].id].values :
                variablesValues[jsonContent[i].id],
              surveyId: jsonContent[i].surveyId,
              conceptId: jsonContent[i].conceptId,
              dataSetIds: ArrayParser.getParsedArray(jsonContent[i].dataSetIds),
            };
            variablesObjArray.push(new Variable(variableObj));
          }
        }
      }
    });
    return variablesObjArray;
  };
  return {
      getVariables: getVariables
    };
});
