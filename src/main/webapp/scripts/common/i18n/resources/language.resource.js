'use strict';

angular.module('metadatamanagementApp')
  .factory('LanguageResource', function($resource) {
    return $resource('/api/i18n/languages', null, {
      'get': {
        isArray: true
      }
    });
  });
