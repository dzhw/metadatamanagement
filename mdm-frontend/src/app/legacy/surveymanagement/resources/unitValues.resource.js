'use strict';

angular.module('metadatamanagementApp').factory('UnitValuesResource', ['$resource', 
  function($resource) {
    return $resource('/api/surveys/unit-values');
  }]);

