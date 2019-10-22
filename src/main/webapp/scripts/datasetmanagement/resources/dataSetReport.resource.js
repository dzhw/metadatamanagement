'use strict';

angular.module('metadatamanagementApp')
  .factory('DataSetReportResource', function($resource) {
    return $resource('api/data-sets/:dataSetId/report/generate/:version', {
      dataSetId: '@dataSetId',
      version: '@version'
    }, {
      'startGeneration': {
        method: 'POST',
        params: {languages: '@languages'}
      }
    });
  });
