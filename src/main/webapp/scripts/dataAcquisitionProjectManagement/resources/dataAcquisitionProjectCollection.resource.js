'use strict';

angular.module('metadatamanagementApp')
    .factory('DataAcquisitionProjectCollection', function($resource) {
      return $resource('/api/data_acquisition_projects',
        {projection: 'complete'}, {
        'query': {
          method: 'GET',
        },
      });
    });
