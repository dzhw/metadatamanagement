'use strict';

angular.module('metadatamanagementApp')
  .controller('GeographicCoverageDisplayController', function($scope,
      LanguageService) {

    $scope.getDescription = function() {
      if ($scope.description) {
        return $scope.description[LanguageService.getCurrentInstantly()];
      } else {
        return '';
      }
    };
  });
