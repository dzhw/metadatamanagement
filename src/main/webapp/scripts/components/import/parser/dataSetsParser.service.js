'use strict';

angular.module('metadatamanagementApp').service('DataSetsParser',
function(DataSet, ArrayParser) {
  var getDataSets = function(dataSets, projectId) {
      var datasetsObjArray = [];
      for (var i = 0; i < dataSets.length; i++) {
        var data = dataSets[i];
        var dataSetObj = {
            id: data.id,
            dataAcquisitionProjectId: projectId,
            description: {
              en: data['description.en'],
              de: data['description.de']
            },
            variableIds: ArrayParser.getParsedArray(data.variableIds),
            surveyIds: ArrayParser.getParsedArray(data.surveyIds)
          };
        datasetsObjArray[i] = new DataSet(dataSetObj);
      }
      return datasetsObjArray;
    };
  return {
      getDatasets: getDataSets
    };
});
