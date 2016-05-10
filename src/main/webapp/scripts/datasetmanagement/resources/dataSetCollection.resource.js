'use strict';

angular.module('metadatamanagementApp')
  .factory('DataSetsCollectionResource', function($resource) {
    return $resource('/api/data-sets', {
      projection: 'complete'
    }, {
      'query': {
        method: 'GET',
      }
    });
  });
