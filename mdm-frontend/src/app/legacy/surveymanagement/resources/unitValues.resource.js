'use strict';

angular.module('metadatamanagementApp').factory('UnitValuesResource',
  function($resource) {
    return $resource('/api/surveys/unit-values');
  });
