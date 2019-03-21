/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('SampleTypePickerController',
  function($scope, SAMPLE_CONTROLLED_VOCABULARY, LanguageService) {

    $scope.language = LanguageService.getCurrentInstantly();
    $scope.selectedSampleType = $scope.sampleType;
    $scope.searchSampleTypes = function(sampleTypeSearchText) {
      return _.filter(SAMPLE_CONTROLLED_VOCABULARY, function(sampleType) {
        return sampleType[$scope.language].indexOf(sampleTypeSearchText) !== -1;
      });
    };
    $scope.onSampleTypeChange = function(sampleType) {
      $scope.sampleType = sampleType;
    };
  });
