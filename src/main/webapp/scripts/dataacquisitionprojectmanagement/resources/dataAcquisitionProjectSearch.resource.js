/* @Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectSearchResource', function($resource) {
    return $resource('/api/data-acquisition-projects/search/findAll', {
      projection: 'complete'
    }, {
      'query': {
        method: 'GET',
      },
    });
  });
