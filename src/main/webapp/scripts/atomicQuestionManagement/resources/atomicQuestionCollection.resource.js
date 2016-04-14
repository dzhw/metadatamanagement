'use strict';

angular.module('metadatamanagementApp')
  .factory('AtomicQuestionCollection', function($resource) {
    return $resource('/api/atomic-questions', {
      projection: 'complete'
    }, {
      'query': {
        method: 'GET',
      }
    });
  });
