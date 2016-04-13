'use strict';

angular.module('metadatamanagementApp').service('DataSetBuilderService',
function(DataSet, CleanJSObjectService) {
  var getDataSets = function(dataSets, projectId) {
      var dataSetsObjArray = [];
      for (var i = 0; i < dataSets.length; i++) {
        var data = dataSets[i];
        var dataSetObj = {
            id: data.id,
            dataAcquisitionProjectId: projectId,
            description: {
              en: data['description.en'],
              de: data['description.de']
            },
            variableIds:
            CleanJSObjectService.removeWhiteSpace(data.variableIds),
            surveyIds: CleanJSObjectService.removeWhiteSpace(data.surveyIds)
          };
        var cleanedDataSetObject = CleanJSObjectService
            .removeEmptyJsonObjects(dataSetObj);
        dataSetsObjArray[i] = new DataSet(cleanedDataSetObject);
      }
      return dataSetsObjArray;
    };
  return {
      getDataSets: getDataSets
    };
});
