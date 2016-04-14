'use strict';

angular.module('metadatamanagementApp')
  .factory('DataSetsCollection', function($resource) {
    return $resource('/api/data-sets', {
      projection: 'complete'
    }, {
      'query': {
        method: 'GET',
      }
    });
  });
