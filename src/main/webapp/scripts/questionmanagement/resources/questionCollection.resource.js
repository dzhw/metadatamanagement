'use strict';

angular.module('metadatamanagementApp')
  .factory('QuestionCollectionResource', function($resource) {
    return $resource('/api/questions', {
      projection: 'complete'
    }, {
      'query': {
        method: 'GET',
      }
    });
  });
