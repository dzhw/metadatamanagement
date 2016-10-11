'use strict';

angular.module('metadatamanagementApp').service('DataSetBuilderService',
  function(DataSetResource, CleanJSObjectService) {
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
          variableIds: CleanJSObjectService.removeWhiteSpace(data.variableIds),
          surveyIds: CleanJSObjectService.removeWhiteSpace(data.surveyIds),
          type: {
            en: 'Personal Record',
            de: 'Personendatensatz'
          },
          subDataSets: [{name: 'abs2005-ds1',description: {de:
            'Personendatensatz Absolventenpanel 2005',
            en: 'Personal record data - Graduates 2005'},
            accessWay: 'remote-desktop-suf'}]
        };
        var cleanedDataSetObject = CleanJSObjectService
          .removeEmptyJsonObjects(dataSetObj);
        dataSetsObjArray[i] = new DataSetResource(cleanedDataSetObject);
      }
      return dataSetsObjArray;
    };
    return {
      getDataSets: getDataSets
    };
  });
