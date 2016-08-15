'use strict';

angular.module('metadatamanagementApp')
  .factory('QuestionCollectionResource', function($resource) {
    return $resource('/api/variables', {
      projection: 'complete'
    }, {
      'query': {
        method: 'GET',
      }
    });
  });
