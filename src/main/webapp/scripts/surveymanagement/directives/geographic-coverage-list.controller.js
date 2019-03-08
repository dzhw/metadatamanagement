/* globals _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('GeographicCoverageListController', function($scope) {

    var updateModelCtrl = function() {
      if ($scope.ngModelCtrl) {
        $scope.ngModelCtrl.$setDirty();
        $scope.ngModelCtrl.$validate();
      }
    };

    $scope.onAddGeographicCoverage = function() {
      var geographicCoverage = {
        country: null,
        description: {
          de: null,
          en: null
        }
      };
      if (angular.isDefined($scope.geographicCoverages)) {
        $scope.geographicCoverages.push(geographicCoverage);
      } else {
        $scope.geographicCoverages = [geographicCoverage];
      }
      updateModelCtrl();
    };

    $scope.deleteGeographicCoverage = function(geographicCoverage) {
      _.remove($scope.geographicCoverages, geographicCoverage);
      if ($scope.ngModelCtrl) {
        $scope.ngModelCtrl.$setDirty();
        updateModelCtrl();
      }
    };
  });
