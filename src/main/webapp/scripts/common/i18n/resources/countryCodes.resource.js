'use strict';

angular.module('metadatamanagementApp').factory('CountryCodesResource',
  function($resource) {
    return $resource('/api/i18n/country-codes');
  });
