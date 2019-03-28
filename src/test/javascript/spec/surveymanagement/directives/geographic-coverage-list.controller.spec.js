/* global describe, beforeEach, spyOn, inject, it, expect */
'use strict';

describe('geographic-coverage-list.controller', function() {

  var $scope;
  var $controller;
  var countryA;
  var countryB;

  beforeEach(module('metadatamanagementApp'));
  beforeEach(inject(function(_$controller_, _$rootScope_) {
    $controller = _$controller_;
    $scope = _$rootScope_.$new();
    var noop = function() {
    };
    $scope.ngModelCtrl = {
      $validate: noop,
      $setDirty: noop
    };
    spyOn($scope.ngModelCtrl, '$setDirty');
    spyOn($scope.ngModelCtrl, '$validate');
    countryA = {
      country: 'US',
      description: {
        de: 'Vereinigte Staaten von Amerika',
        en: 'United States'
      }
    };
    countryB = {
      country: 'DE',
      description: {
        de: 'Deutschland',
        en: 'Germany'
      }
    };
  }));

  var verifyCreatedCountryItem = function(country) {
    expect(country.country).toBeNull();
    expect(country.description.de).toBeNull();
    expect(country.description.en).toBeNull();
  };

  var verifyNgModelCtrlInteraction = function() {
    expect($scope.ngModelCtrl.$validate).toHaveBeenCalled();
    expect($scope.ngModelCtrl.$setDirty).toHaveBeenCalled();
  };

  it('should create a new array if none is defined on $scope', function() {
    $controller('GeographicCoverageListController', {$scope: $scope});
    expect($scope.geographicCoverageList).not.toBeDefined();

    $scope.onAddGeographicCoverage();

    expect($scope.geographicCoverages).toBeDefined();
    expect($scope.geographicCoverages.length).toEqual(1);
    verifyCreatedCountryItem($scope.geographicCoverages[0]);
    verifyNgModelCtrlInteraction();
  });

  it('should push to an existing geographic coverages array', function() {
    var geographicCoverages = [];
    $scope.geographicCoverages = geographicCoverages;
    $controller('GeographicCoverageListController', {$scope: $scope});

    $scope.onAddGeographicCoverage();

    expect($scope.geographicCoverages).toBe(geographicCoverages);
    expect(geographicCoverages.length).toBe(1);
    verifyCreatedCountryItem(geographicCoverages[0]);
    verifyNgModelCtrlInteraction();
  });

  it('should delete a geographic coverage item from the array', function() {
    $scope.geographicCoverages = [countryA, countryB];
    $controller('GeographicCoverageListController', {$scope: $scope});

    $scope.deleteGeographicCoverage(countryA);

    expect($scope.geographicCoverages.length).toEqual(1);
    expect($scope.geographicCoverages[0]).toBe(countryB);
    verifyNgModelCtrlInteraction();
  });

  it('should move an item up in the list', function() {
    $scope.geographicCoverages = [countryA, countryB];
    $controller('GeographicCoverageListController', {$scope: $scope});

    $scope.moveItemUp(1);

    expect($scope.geographicCoverages.length).toEqual(2);
    expect($scope.geographicCoverages[0]).toEqual(countryB);
    expect($scope.geographicCoverages[1]).toEqual(countryA);
    verifyNgModelCtrlInteraction();
  });

  it('should move an item down in the list', function() {
    $scope.geographicCoverages = [countryA, countryB];
    $controller('GeographicCoverageListController', {$scope: $scope});

    $scope.moveItemDown(0);

    expect($scope.geographicCoverages.length).toEqual(2);
    expect($scope.geographicCoverages[0]).toEqual(countryB);
    expect($scope.geographicCoverages[1]).toEqual(countryA);
    verifyNgModelCtrlInteraction();
  });
});
