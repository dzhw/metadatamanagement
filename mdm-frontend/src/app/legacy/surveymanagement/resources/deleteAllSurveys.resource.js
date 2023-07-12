'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllSurveysResource', function($resource) {
    return $resource('/api/data-acquisition-projects/:id/surveys', {
      id: '@id'
    }, {
      'deleteAll': {
        method: 'DELETE'
      }
    });
  });
