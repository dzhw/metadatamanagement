/* global describe, beforeEach, spyOn, inject, it, expect, jasmine */
'use strict';

describe('geographic-coverage.controller', function() {
  var countryQueryResult = [
    {
      'code': 'DE',
      'de': 'Deutschland',
      'en': 'Germany'
    },
    {
      'code': 'US',
      'de': 'Vereinigte Staaten von Amerika',
      'en': 'United States'
    }
  ];
  var $scope;
  var $controller;
  var $httpBackend;
  var expectedCountry = countryQueryResult[0];

  beforeEach(module('metadatamanagementApp'));
  beforeEach(mockSso);
  beforeEach(inject(function(_$controller_, _$rootScope_, _LanguageService_,
                             _$httpBackend_) {
    $controller = _$controller_;
    $scope = _$rootScope_.$new();
    spyOn(_LanguageService_, 'getCurrentInstantly').and.returnValue('de');
    $scope.geographicCoverage = {
      country: 'DE'
      //description ...
    };
    $httpBackend = _$httpBackend_;
    $httpBackend.when('GET', '/api/i18n/country-codes')
      .respond(countryQueryResult);
  }));

  it('should initialize properly', function() {
    $controller('GeographicCoverageController', {$scope: $scope});

    expect($scope.isDisabled).toBe(true);

    $httpBackend.flush();

    expect($scope.isDisabled).toBe(false);
    expect($scope.language).toEqual('de');
    expect($scope.selectedCountry.code).toEqual(expectedCountry.code);
    expect($scope.selectedCountry.de).toEqual(expectedCountry.de);
    expect($scope.selectedCountry.en).toEqual(expectedCountry.en);
  });

  it('should filter the available country list with the partial country name',
    function() {
      $controller('GeographicCoverageController', {$scope: $scope});
      $httpBackend.flush();

      var filteredCountries = $scope.filterCountries('deu');
      expect(filteredCountries.length).toBe(1);
      expect(filteredCountries[0].code).toEqual(expectedCountry.code);
      expect(filteredCountries[0].de).toEqual(expectedCountry.de);
      expect(filteredCountries[0].en).toEqual(expectedCountry.en);
    });

  it('should filter the available country list with the country code',
    function() {
      $controller('GeographicCoverageController', {$scope: $scope});
      $httpBackend.flush();

      var filteredCountries = $scope.filterCountries('US');
      expect(filteredCountries.length).toBe(1);
      expect(filteredCountries[0]).toEqual(jasmine
        .objectContaining({code: 'US'}));
    });

  it('should set the country on geographic coverage', function() {
    $controller('GeographicCoverageController', {$scope: $scope});

    $scope.selectedCountryChange(countryQueryResult[0]);

    expect($scope.geographicCoverage.country)
      .toEqual(countryQueryResult[0].code);
  });

  it('should clear the country in geographic coverage if no country was given',
    function() {
      $controller('GeographicCoverageController', {$scope: $scope});
      $httpBackend.flush();

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
