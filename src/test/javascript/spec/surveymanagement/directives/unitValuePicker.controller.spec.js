/* globals describe, beforeEach, module, inject, it, expect, spyOn */
'use strict';

describe('UnitValuePickerController', function() {
  var unitValuesResponse = [
    {
      'de': 'Hochschulabsolventen',
      'en': 'College Graduates'
    },
    {
      'de': 'Wissenschaftliches Personal',
      'en': 'Scientific Staff'
    }
  ];
  var $scope;
  var $controller;
  var $httpBackend;
  var noop = function() {

  };
  beforeEach(module('metadatamanagementApp'));
  beforeEach(mockSso);
  beforeEach(inject(function(_$rootScope_, _$controller_, _LanguageService_,
                             _$httpBackend_) {
    $scope = _$rootScope_.$new();
    $controller = _$controller_;
    $httpBackend = _$httpBackend_;
    $httpBackend.when('GET', '/api/surveys/unit-values')
      .respond(200, unitValuesResponse);
    spyOn(_LanguageService_, 'getCurrentInstantly').and.returnValue('de');
  }));

  it('should initialize autocomplete selection with the given unit',
    function() {
      $scope.unit = {
        de: 'Hochschulabsolventen',
        en: 'College Graduates'
      };
      $controller('UnitValuePickerController', {$scope: $scope});
      $httpBackend.flush();

      expect($scope.selectedUnitValue.de).toEqual($scope.unit.de);
      expect($scope.selectedUnitValue.en).toEqual($scope.unit.en);
    });

  it('should initialize with the set language', function() {
    $controller('UnitValuePickerController', {$scope: $scope});

    expect($scope.language).toEqual('de');
  });

  it('should set "unit" field to given unit parameter', function() {
    $controller('UnitValuePickerController', {$scope: $scope});
    $httpBackend.flush();
    $scope.unitValuePickerForm = {
      $setDirty: noop
    };
    $scope.selectedUnitChange(unitValuesResponse[0]);
    expect($scope.unit.de).toEqual(unitValuesResponse[0].de);
    expect($scope.unit.en).toEqual(unitValuesResponse[0].en);
  });

  it('should find the correct unit for the given search string', function() {
    $controller('UnitValuePickerController', {$scope: $scope});
    $httpBackend.flush();

    var result = $scope.filterUnitValues('wiss');

    expect(result).toBeDefined();
    expect(result.length).toBe(1);
    expect(result[0].de).toEqual(unitValuesResponse[1].de);
    expect(result[0].en).toEqual(unitValuesResponse[1].en);
  });

  it('should set $touched on form controls to true if the form is set to ' +
    '$dirty', function() {
    var control = {
      $setTouched: function() {
        // noop
      }
    };
    spyOn(control, '$setTouched');

    $scope.unitValuePickerForm = {
      $dirty: false
    };

    $controller('UnitValuePickerController', {$scope: $scope});
    $scope.$digest();
    $scope.unitValuePickerForm.$error = {
      'required': [control]
    };

    $scope.unitValuePickerForm.$dirty = true;
    $scope.$digest();

    expect(control.$setTouched).toHaveBeenCalled();
  });
});
