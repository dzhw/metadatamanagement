'use strict';

angular.module('metadatamanagementApp')
  .factory('AnalysisPackageVersionsResource', ['$resource',  function($resource) {
    return $resource('/api/analysis-packages/:id/versions', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  }]);

