'use strict';

angular.module('metadatamanagementApp')
  .factory('ScriptSoftwarePackagesResource', ['$resource',  function($resource) {
    return $resource('/api/analysis-packages/scripts/software-packages', null, {
      'get': {
        isArray: true
      }
    });
  }]);

