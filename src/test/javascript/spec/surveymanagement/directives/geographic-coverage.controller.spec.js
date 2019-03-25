/* global describe, beforeEach, spyOn, inject, it, expect, jasmine */
'use strict';

describe('geographic-coverage.controller', function() {
  var $scope;
  var $controller;
  var countries;
  var expectedCountry = {
    code: 'DE',
    en: 'Germany',
    de: 'Deutschland'
  };

  beforeEach(module('metadatamanagementApp'));
  beforeEach(inject(function(_$controller_, _$rootScope_, _LanguageService_,
                             _COUNTRIES_) {
    $controller = _$controller_;
    $scope = _$rootScope_.$new();
    countries = _COUNTRIES_;
    spyOn(_LanguageService_, 'getCurrentInstantly').and.returnValue('de');
    $scope.geographicCoverage = {
      country: 'DE'
      //description ...
    };
  }));

  it('should initialize properly', function() {
    $controller('GeographicCoverageController', {$scope: $scope});

    expect($scope.language).toEqual('de');
    expect($scope.countries).toBe(countries);
    expect($scope.selectedCountry).toEqual(expectedCountry);
  });

  it('should filter the available country list with the partial country name',
    function() {
      $controller('GeographicCoverageController', {$scope: $scope});

      var filteredCountries = $scope.filterCountries('deu');
      expect(filteredCountries.length).toBe(1);
      expect(filteredCountries[0]).toEqual(expectedCountry);
    });

  it('should filter the available country list with the country code',
    function() {
      $controller('GeographicCoverageController', {$scope: $scope});

      var filteredCountries = $scope.filterCountries('US');
      expect(filteredCountries.length).toBe(2);
      expect(filteredCountries[0]).toEqual(jasmine
        .objectContaining({code: 'US'}));
    });

  it('should set the country on geographic coverage', function() {
    $controller('GeographicCoverageController', {$scope: $scope});

    $scope.selectedCountryChange(countries[0]);

    expect($scope.geographicCoverage.country).toEqual(countries[0].code);
  });

  it('should clear the country in geographic coverage if no country was given',
    function() {
      $controller('GeographicCoverageController', {$scope: $scope});

      $scope.selectedCountryChange(undefined);

      expect($scope.geographicCoverage.country).toBeNull();
    });

  it('should set $touched state on all invalid controls on ' +
    'geographicCoverageForm.$dirty state changes', function() {

    var control = {
      $setTouched: function() {
        // noop
      }
    };
    spyOn(control, '$setTouched');

    $scope.geographicCoverageForm = {
      $dirty: false
    };

    $controller('GeographicCoverageController', {$scope: $scope});
    $scope.$digest();
    $scope.geographicCoverageForm.$error = {
      'validator-key': [control]
    };

    $scope.geographicCoverageForm.$dirty = true;
    $scope.$digest();

    expect(control.$setTouched).toHaveBeenCalled();
  });
});
