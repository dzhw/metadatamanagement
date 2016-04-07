'use strict';

angular.module('metadatamanagementApp').service('DataSetBuilder',
function(DataSet, ParserUtil) {
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
            variableIds: ParserUtil.getParsedArray(data.variableIds),
            surveyIds: ParserUtil.getParsedArray(data.surveyIds)
          };
        var cleanedDataSetObject = ParserUtil
            .removeEmptyJsonObjects(dataSetObj);
        dataSetsObjArray[i] = new DataSet(cleanedDataSetObject);
      }
      return dataSetsObjArray;
    };
  return {
      getDataSets: getDataSets
    };
});
