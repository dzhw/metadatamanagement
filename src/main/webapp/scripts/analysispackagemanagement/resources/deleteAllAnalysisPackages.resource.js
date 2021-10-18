'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllAnalysisPackagesResource', function($resource) {
    return $resource('/api/analysis-acquisition-projects/:id/analysis-packages', {
      id: '@id'
    }, {
      'deleteAll': {
        method: 'DELETE'
      }
    });
  });
