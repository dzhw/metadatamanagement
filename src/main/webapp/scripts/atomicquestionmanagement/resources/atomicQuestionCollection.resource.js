'use strict';

angular.module('metadatamanagementApp')
  .factory('AtomicQuestionCollectionResource', function($resource) {
    return $resource('/api/atomic-questions', {
      projection: 'complete'
    }, {
      'query': {
        method: 'GET',
      }
    });
  });
