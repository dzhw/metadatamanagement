'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllAnalysisPackagesResource', ['$resource',  function($resource) {
    return $resource('/api/data-acquisition-projects/:id/' +
      'analysis-packages', {
      id: '@id'
    }, {
      'deleteAll': {
        method: 'DELETE'
      }
    });
  }]);

