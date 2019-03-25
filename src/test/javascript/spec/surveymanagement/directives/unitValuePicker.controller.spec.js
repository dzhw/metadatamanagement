/* globals describe, beforeEach, module, inject, it, expect, spyOn */
'use strict';

describe('UnitValuePickerController', function() {
  var $scope;
  var $controller;
  var unitValues;
  beforeEach(module('metadatamanagementApp'));
  beforeEach(inject(function(_$rootScope_, _$controller_, _LanguageService_,
                             _UNIT_VALUES_) {
    $scope = _$rootScope_.$new();
    $controller = _$controller_;
    unitValues = _UNIT_VALUES_;
    spyOn(_LanguageService_, 'getCurrentInstantly').and.returnValue('de');
  }));

  it('should initialize autocomplete selection with the given unit',
    function() {
      $scope.unit = {
        de: 'Hochschulabsolventen',
        en: 'College Graduates'
      };

      $controller('UnitValuePickerController', {$scope: $scope});

      expect($scope.selectedUnitValue).toEqual($scope.unit);
    });

  it('should initialize with the set language', function() {
    $controller('UnitValuePickerController', {$scope: $scope});

    expect($scope.language).toEqual('de');
  });

  it('should set "unit" field to given unit parameter', function() {
    $controller('UnitValuePickerController', {$scope: $scope});
    $scope.selectedUnitChange(unitValues[0]);
    expect($scope.unit).toBe(unitValues[0]);
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
