/* globals _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('GeographicCoverageController', function($scope, $mdMedia, $log,
     CountryCodesResource, LanguageService) {

    var countries;

    $scope.isDisabled = true;
    $scope.isScreenGreaterSmall = $mdMedia('gt-sm');
    $scope.language = LanguageService.getCurrentInstantly();

    if ($scope.language !== 'de' && $scope.language !== 'en') {
      $log.warn('No localization found for language "' +
        $scope.language + '", will use english (\'en\') as fallback for ' +
        'country names');
      $scope.language = 'en';
    }

    CountryCodesResource.query().$promise.then(function(result) {
      countries = _.sortBy(result, function(country) {
        return country[$scope.language];
      });
      $scope.isDisabled = false;
      $scope.selectedCountry = _.find(result, function(country) {
        return country.code === $scope.geographicCoverage.country;
      });
    });

    $scope.filterCountries = function(countryStr) {
      return _.filter(countries, function(country) {
        return country.code.startsWith(countryStr) || country[$scope.language]
          .toLowerCase().startsWith(countryStr.toLowerCase());
      });
    };

    $scope.selectedCountryChange = function(country) {
      if (country) {
        $scope.geographicCoverage.country = country.code;
      } else {
        $scope.geographicCoverage.country = null;
      }
    };

    $scope.$watch('geographicCoverageForm.$dirty',
      function(newVal, oldVal) {
        if (newVal !== oldVal && newVal) {
          angular.forEach($scope.geographicCoverageForm.$error,
            function(errorCategory) {
              angular.forEach(errorCategory, function(control) {
                control.$setTouched();
              });
            });
        }
      });
  });
