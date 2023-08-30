'use strict';

angular.module('metadatamanagementApp').factory('SurveySampleTypeResource', ['$resource', 
  function($resource) {
    return $resource('/api/surveys/sample-types');
  }]);

