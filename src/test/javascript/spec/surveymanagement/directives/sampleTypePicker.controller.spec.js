/* globals describe, beforeEach, inject, it, spyOn, expect, mockSso */
'use strict';

describe('SurveySampleTypePicker', function() {

  var sampleTypesResponse = [
    {
      'de': 'Wahrscheinlichkeitsauswahl: Geschichtete Klumpenauswahl',
      'en': 'Probability Sample: Stratified Cluster Sampie'
    },
    {
      'de': 'Kombination aus Wahrscheinlichkeits- und Nicht-' +
        'Wahrscheinlichkeitsauswahl',
      'en': 'Mixed probability and non-probability Sample'
    }
  ];
  var $scope;
  var $controller;
  var $httpBackend;
  var noop = function() {};

  beforeEach(module('metadatamanagementApp'));
  beforeEach(mockSso);
  beforeEach(inject(function(_$rootScope_, _$controller_, _$httpBackend_,
                             _LanguageService_) {
    $scope = _$rootScope_.$new();
    $controller = _$controller_;
    $httpBackend = _$httpBackend_;
    $httpBackend
      .when('GET', '/api/surveys/sample-types')
      .respond(sampleTypesResponse);

    spyOn(_LanguageService_, 'getCurrentInstantly').and.returnValue('de');
  }));

  it('should initialize properly', function() {
    $scope.sampleType = sampleTypesResponse[0];
    $controller('SampleTypePickerController', {$scope: $scope});

    expect($scope.isDisabled).toBe(true);
    expect($scope.selectedSampleType).toEqual(sampleTypesResponse[0]);
    expect($scope.language).toEqual('de');

    $httpBackend.flush();

    expect($scope.isDisabled).toBe(false);
  });

  it('should set sampleType field to the given sample type parameter',
    function() {
      $controller('SampleTypePickerController', {$scope: $scope});
      $scope.sampleTypeForm = {
        $setDirty: noop
      };
      $scope.onSampleTypeChange(sampleTypesResponse[0]);

      expect($scope.sampleType).toBe(sampleTypesResponse[0]);
    });

  it('should filter available sample types by the given search string',
    function() {
      $controller('SampleTypePickerController', {$scope: $scope});
      $httpBackend.flush();
      var result = $scope.searchSampleTypes('klumpen');

      expect(result).toBeDefined();
      expect(result.length).toBe(1);
      /* Resource response is wrapped in a Response object, we therefore do a
      field comparison */
      expect(result[0].de).toEqual(sampleTypesResponse[0].de);
      expect(result[0].en).toEqual(sampleTypesResponse[0].en);
    });

  it('should set invalid form controls to touched if the form state changes ' +
    'to dirty', function() {
    var control = {
      $setTouched: function() {
        // noop
      }
    };
    spyOn(control, '$setTouched');

    $scope.sampleTypeForm = {
      $dirty: false
    };

    $controller('SampleTypePickerController', {$scope: $scope});
    $scope.$digest();
    $scope.sampleTypeForm.$error = {
      'validator-key': [control]
    };

    $scope.sampleTypeForm.$dirty = true;
    $scope.$digest();

    expect(control.$setTouched).toHaveBeenCalled();
  });
});
