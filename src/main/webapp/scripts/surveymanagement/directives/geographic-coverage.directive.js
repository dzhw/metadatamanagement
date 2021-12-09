'use strict';

angular.module('metadatamanagementApp')
  .directive('geographicCoverage', function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/surveymanagement/directives/geographic-coverage' +
        '.html.tmpl',
      scope: {
        geographicCoverage: '='
      },
      controller: 'GeographicCoverageController'
    };
  });
