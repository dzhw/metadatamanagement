'use strict';

angular.module('metadatamanagementApp')
    .factory('FdzProjectCollection', function($resource) {
      return $resource('/api/fdz_projects',
        {projection: 'complete'}, {
        'query': {
          method: 'GET',
        },
      });
    });
