'use strict';

angular.module('metadatamanagementApp').service('DataSetsParser',
function(DataSet) {
  var getDataSets = function(dataSets, projectId) {
      var datasetsObjArray = [];
      for (var i = 0; i < dataSets.length; i++) {
        var data = dataSets[i];
        if (!data.id || data.id === '') {
          /*$scope.uploadStatus.pushError($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.' +
            'missingId', {
              index: i + 1
            }));*/
          console.log('sd');
        } else {
          var dataSetObj = {
            id: data.id,
            dataAcquisitionProjectId: projectId,
            questionnaireId: data.questionnaireId,
            description: {
              en: data['description.en'],
              de: data['description.de']
            },
            variableIds: data.variableIds.replace(/ /g, '').split(
              ','),
            surveyIds: data.surveyIds.replace(/ /g, '').split(',')
          };
          datasetsObjArray[i] = new DataSet(dataSetObj);
        }
      }
      return datasetsObjArray;
    };
  return {
      getDatasets: getDataSets
    };
});
