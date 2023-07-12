/* globals _ */
'use strict';

angular.module('metadatamanagementApp').controller('UnitValuePickerController',
  function($scope, LanguageService, UnitValuesResource) {
    var unitValues;

    $scope.isDisabled = true;
    $scope.language = LanguageService.getCurrentInstantly();
    $scope.selectedUnitValue = $scope.unit;

    UnitValuesResource.query().$promise.then(function(result) {
      unitValues = _.sortBy(result, function(unit) {
        return unit[$scope.language];
      });
      $scope.isDisabled = false;
    });

    $scope.filterUnitValues = function(unitValueStr) {
      return _.filter(unitValues, function(unitValue) {
        return unitValue[$scope.language].toLowerCase()
          .indexOf(unitValueStr.toLowerCase()) !== -1;
      });
    };
    $scope.selectedUnitChange = function(unit) {
      $scope.unit = unit;
      $scope.unitValuePickerForm.$setDirty();
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
