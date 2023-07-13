'use strict';

angular.module('metadatamanagementApp').factory('CountryCodesResource', ['$resource', 
  function($resource) {
    return $resource('/api/i18n/country-codes');
  }]);

