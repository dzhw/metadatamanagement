/* globals _ */
'use strict';

angular.module('metadatamanagementApp').controller('UnitValuePickerController',
  function($scope, LanguageService, UNIT_VALUES) {
    if ($scope.unit) {
      $scope.selectedUnitValue = _.find(UNIT_VALUES, $scope.unit);
    }
    $scope.language = LanguageService.getCurrentInstantly();
    $scope.filterUnitValues = function(unitValueStr) {
      return _.filter(UNIT_VALUES, function(unitValue) {
        return unitValue[$scope.language].toLowerCase()
          .indexOf(unitValueStr.toLowerCase()) !== -1;
      });
    };
    $scope.selectedUnitChange = function(unit) {
      $scope.unit = unit;
    };
    $scope.$watch('unitValuePickerForm.$dirty',
      function(newVal, oldVal) {
        if (newVal !== oldVal && newVal) {
          angular.forEach($scope.unitValuePickerForm.$error,
            function(errorCategory) {
              angular.forEach(errorCategory, function(control) {
                control.$setTouched();
              });
            });
        }
      });
  });
