'use strict';

angular.module('metadatamanagementApp').directive('geographicCoverageDisplay',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/surveymanagement/directives/geographic-coverage' +
        '-display.html.tmpl',
      controller: 'GeographicCoverageDisplayController',
      scope: {
        countryName: '=',
        description: '=?'
      }
    };
  });
