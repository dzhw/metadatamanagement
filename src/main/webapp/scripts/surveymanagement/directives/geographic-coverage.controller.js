/* globals _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('GeographicCoverageController', function($scope, $mdMedia, $log,
      COUNTRIES, LanguageService) {

  var init = function() {
    $scope.isScreenGreaterSmall = $mdMedia('gt-sm');
    $scope.countries = COUNTRIES;
    var userLanguage = LanguageService.getCurrentInstantly();

    if (userLanguage !== 'de' && userLanguage !== 'en') {
      $log.warn('No localization found for language "' +
        userLanguage + '", will use english (\'en\') as fallback for ' +
        'country names');
      userLanguage = 'en';
    }

    $scope.language = userLanguage;

    $scope.selectedCountry = _.find(COUNTRIES, function(country) {
      return country.code === $scope.geographicCoverage.country;
    });
  };
  init();

  $scope.filterCountries = function(countryStr) {
    return _.filter(COUNTRIES, function(country) {
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
