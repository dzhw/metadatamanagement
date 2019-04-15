'use strict';

angular.module('metadatamanagementApp').factory('SurveySampleTypeResource',
  function($resource) {
    return $resource('/api/surveys/sample-types');
  });
