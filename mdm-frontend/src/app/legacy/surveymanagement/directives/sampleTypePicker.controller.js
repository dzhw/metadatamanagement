/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('SampleTypePickerController', [
  '$scope',
  'LanguageService',
  'SurveySampleTypeResource',
  function($scope, LanguageService, SurveySampleTypeResource) {

    var sampleTypes = null;
    $scope.isDisabled = true;
    $scope.language = LanguageService.getCurrentInstantly();
    $scope.selectedSampleType = $scope.sampleType;
    $scope.searchSampleTypes = function(sampleTypeSearchText) {
      return _.filter(sampleTypes, function(sampleType) {
        return sampleType[$scope.language].toLowerCase()
          .indexOf(sampleTypeSearchText.toLowerCase()) !== -1;
      });
    };
    $scope.onSampleTypeChange = function(sampleType) {
      $scope.sampleType = sampleType;
      $scope.sampleTypeForm.$setDirty();
    };

    $scope.$watch('sampleTypeForm.$dirty',
      function(newVal, oldVal) {
        if (newVal !== oldVal && newVal) {
          angular.forEach($scope.sampleTypeForm.$error,
            function(errorCategory) {
              angular.forEach(errorCategory, function(control) {
                control.$setTouched();
              });
            });
        }
      });

    SurveySampleTypeResource.query().$promise.then(function(
      result) {
      sampleTypes = _.sortBy(result, [function(item) {
        return item[$scope.language];
      }]);
      $scope.isDisabled = false;
    });
  }]);

