'use strict';

angular.module('metadatamanagementApp')
  .factory('AnalysisPackageOverviewResource', ['$resource',  function($resource) {
    return $resource(
      'api/analysis-packages/:analysisPackageId/overview/generate/:version', {
      dataPackageId: '@analysisPackageId',
      version: '@version'
    }, {
      'startGeneration': {
        method: 'POST',
        params: {languages: '@languages'}
      }
    });
  }]);

