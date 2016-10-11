'use strict';

angular.module('metadatamanagementApp').service('DataSetBuilderService',
    function(DataSetResource, CleanJSObjectService) {
      var buildDataSet = function(dataSet, subDataSets,
        dataAcquisitionProjectId) {
        if (!dataSet || !dataAcquisitionProjectId) {
          return null;
        }

        var dataSetObj = {
            id: dataSet.id,
            dataAcquisitionProjectId: dataAcquisitionProjectId,
            description: {
              en: dataSet['description.en'],
              de: dataSet['description.de']
            },
            variableIds:
            CleanJSObjectService.removeWhiteSpace(dataSet.variableIds),
            surveyIds: CleanJSObjectService.removeWhiteSpace(dataSet.surveyIds),
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
      var buildSubDataSets = function(subDataSets) {
        var subDataSetsArray = [];
        for (var i = 0; i < subDataSets.length; i++) {
          var subDataSet = subDataSets[i];
          var subDataSetObj = {
            name: subDataSet.name,
            description: {
              de: subDataSet['description.de'],
              en: subDataSet['description.en']
            },
            accessWay: subDataSet.accessWay
          };
          var cleanedDataSetObject = CleanJSObjectService
          .removeEmptyJsonObjects(subDataSetObj);
          subDataSetsArray[i] = cleanedDataSetObject;
        }
        return subDataSetsArray;
      };
      return {
        buildDataSet: buildDataSet,
        buildSubDataSets: buildSubDataSets
      };
    });
