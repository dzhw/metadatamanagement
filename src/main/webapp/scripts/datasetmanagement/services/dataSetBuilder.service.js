/* global _*/
'use strict';

angular.module('metadatamanagementApp').service('DataSetBuilderService',
  function(DataSetResource, CleanJSObjectService) {
    var buildDataSet = function(dataSet,
      dataAcquisitionProjectId) {
      if (!dataSet || !dataAcquisitionProjectId) {
        return null;
      }

      //Create DataSet Object
      var dataSetObj = {
        id: dataAcquisitionProjectId + '-ds' + dataSet.number,
        dataAcquisitionProjectId: dataAcquisitionProjectId,
        description: {
          en: dataSet['description.en'],
          de: dataSet['description.de']
        },
        number: dataSet.number,
        surveyNumbers: (dataSet.surveyNumbers + '').
        split(','),
        surveyIds: [],
        type: {
          en: dataSet['type.en'],
          de: dataSet['type.de']
        },
        format: {
          en: dataSet['format.en'],
          de: dataSet['format.de']
        },
        subDataSets: dataSet.subDataSets
      };
      _.forEach(dataSetObj.surveyNumbers, function(number) {
        dataSetObj.surveyIds
          .push(dataAcquisitionProjectId + '-sy' + number);
      });
      var cleanedDataSetObject = CleanJSObjectService
        .removeEmptyJsonObjects(dataSetObj);
      return new DataSetResource(cleanedDataSetObject);
    };

    //Create SubdataSet
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
        subDataSetErrors.push(error);
      }
      if (!Number(subDataSet.numberOfAnalyzedVariables)) {
        error = {
          message: 'data-set-management.log-messages.data-set.sub-' +
            'data-set.number-of-analyzed-variables-parse-error',
          translationParams: {
            name: subDataSet.name
          }
        };
        subDataSetErrors.push(error);
      }

      if (subDataSetErrors.length === 0) {
        var subDataSetObj = {
          name: subDataSet.name,
          description: {
            de: subDataSet['description.de'],
            en: subDataSet['description.en']
          },
          accessWay: subDataSet.accessWay,
          numberOfObservations: parseInt(subDataSet.numberOfObservations),
          numberOfAnalyzedVariables: parseInt(
            subDataSet.numberOfAnalyzedVariables),
          dataSetNumber: subDataSet.dataSetNumber
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
