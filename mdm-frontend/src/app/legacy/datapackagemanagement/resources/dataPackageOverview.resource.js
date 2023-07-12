'use strict';

angular.module('metadatamanagementApp')
  .factory('DataPackageOverviewResource', function($resource) {
    return $resource(
      'api/data-packages/:dataPackageId/overview/generate/:version', {
      dataPackageId: '@dataPackageId',
      version: '@version'
    }, {
      'startGeneration': {
        method: 'POST',
        params: {languages: '@languages'}
      }
    });
  });
