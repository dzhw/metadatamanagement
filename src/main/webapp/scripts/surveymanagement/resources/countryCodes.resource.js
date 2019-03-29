'use strict';

angular.module('metadatamanagementApp').factory('CountryCodesResource',
  function($resource) {
    return $resource('/api/surveys/country-codes');
  });
