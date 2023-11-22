'use strict';

angular.module('metadatamanagementApp')
  .factory('LanguageResource', ['$resource',  function($resource) {
    return $resource('/api/i18n/languages', null, {
      'get': {
        isArray: true
      }
    });
  }]);

