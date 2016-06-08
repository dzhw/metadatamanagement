'use strict';

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectCollectionResource', function($resource) {
    return $resource('/api/data-acquisition-projects', {
      projection: 'complete'
    }, {
      'query': {
        method: 'GET',
      },
    });
  });
