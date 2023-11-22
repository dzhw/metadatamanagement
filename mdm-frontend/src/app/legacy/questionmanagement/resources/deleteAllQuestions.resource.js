'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllQuestionsResource', ['$resource',  function($resource) {
    return $resource('/api/data-acquisition-projects/:id/questions', {
      id: '@id'
    }, {
      'deleteAll': {
        method: 'DELETE'
      }
    });
  }]);

