'use strict';

angular.module('metadatamanagementApp')
    .factory('FdzProjectCollection', function($resource) {
      return $resource('/api/fdz_projects',
        {}, {
        'query': {
          method: 'GET',
        },
      });
    });
