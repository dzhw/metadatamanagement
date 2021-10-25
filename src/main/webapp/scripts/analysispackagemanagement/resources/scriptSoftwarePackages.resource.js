'use strict';

angular.module('metadatamanagementApp')
  .factory('ScriptSoftwarePackagesResource', function($resource) {
    return $resource('/api/analysis-packages/scripts/software-packages', {}, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });
