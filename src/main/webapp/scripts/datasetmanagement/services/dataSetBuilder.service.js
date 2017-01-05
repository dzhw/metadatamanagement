'use strict';

angular.module('metadatamanagementApp').service('DataSetBuilderService',
    function(DataSetResource, CleanJSObjectService) {
      var buildDataSet = function(dataSet, subDataSets,
        dataAcquisitionProjectId) {
        if (!dataSet || !dataAcquisitionProjectId) {
          return null;
        }

        var surveyIds = [];
        var surveyNumberArray = dataSet.surveyNumbers.split(',');
        surveyNumberArray.forEach(function(surveyNumber) {
          console.log(surveyNumber);
          surveyIds.push(dataAcquisitionProjectId + '-sy' + surveyNumber);
        });

        var dataSetObj = {
            id: dataAcquisitionProjectId + '-ds' + dataSet.number,
            dataAcquisitionProjectId: dataAcquisitionProjectId,
            description: {
              en: dataSet['description.en'],
              de: dataSet['description.de']
            },
            number: dataSet.number,
            surveyNumbers: CleanJSObjectService
              .removeWhiteSpace(dataSet.surveyNumbers),
            surveyIds: surveyIds,
            type: {
              en: dataSet['type.en'],
              de: dataSet['type.de']
            },
            subDataSets: subDataSets
          };
        var cleanedDataSetObject = CleanJSObjectService
        .removeEmptyJsonObjects(dataSetObj);
        return new DataSetResource(cleanedDataSetObject);
      };
      var buildSubDataSet = function(subDataSet) {
          var subDataSetErrors = [];
          var error;
          if (!Number(subDataSet.numberOfObservations)) {
            error = {
              message: 'data-set-management.log-messages.data-set.sub-' +
              'data-set.number-of-observations-parse-error',
              translationParams: {
                name: subDataSet.name
              }
            };
            subDataSetErrors .push(error);
          }
          if (!Number(subDataSet.numberOfAnalyzedVariables)) {
            error = {
              message: 'data-set-management.log-messages.data-set.sub-' +
              'data-set.number-of-analyzed-variables-parse-error',
              translationParams: {
                name: subDataSet.name
              }
            };
            subDataSetErrors .push(error);
          }
          if (subDataSetErrors.length === 0) {
            var subDataSetObj = {
                name: subDataSet.name,
                description: {
                  de: subDataSet['description.de'],
                  en: subDataSet['description.en']
                },
                accessWay: subDataSet.accessWay,
                numberOfObservations:
                parseInt(subDataSet.numberOfObservations),
                numberOfAnalyzedVariables:
                parseInt(subDataSet.numberOfAnalyzedVariables)
              };
            var cleanedSubDataSetObject = CleanJSObjectService
              .removeEmptyJsonObjects(subDataSetObj);
            return cleanedSubDataSetObject;
          } else {
            throw subDataSetErrors;
          }
        };
      return {
        buildDataSet: buildDataSet,
        buildSubDataSet: buildSubDataSet
      };
    });
