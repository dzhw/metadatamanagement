'use strict';

angular.module('metadatamanagementApp')
  .factory('AnalysisPackageAccessWaysResource', function($resource) {
    return $resource('/api/analysis-packages/:id/access-ways', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });
