'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllSurveysResource', ['$resource',  function($resource) {
    return $resource('/api/data-acquisition-projects/:id/surveys', {
      id: '@id'
    }, {
      'deleteAll': {
        method: 'DELETE'
      }
    });
  }]);

