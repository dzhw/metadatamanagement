'use strict';
/* Actually no use */
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
