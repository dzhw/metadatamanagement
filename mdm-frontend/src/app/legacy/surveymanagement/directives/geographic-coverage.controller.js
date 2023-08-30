/* globals _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('GeographicCoverageController', [
  '$scope',
  '$mdMedia',
  '$log',
  'CountryCodesResource',
  'LanguageService', function($scope, $mdMedia, $log,
     CountryCodesResource, LanguageService) {

    var countries;
    var initComplete = false;

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
      $scope.selectedCountry = _.find(result, function(country) {
        return country.code === $scope.geographicCoverage.country;
      });
      $scope.isDisabled = false;
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

      /*
       * This function is triggered due to the delayed initialization caused by
       * a request for countries. To prevent the form from becoming dirty we
       * ignore the first call to this function and listen on real user
       * interactions afterwards.
       */
      if (!initComplete) {
        initComplete = true;
      } else {
        $scope.geographicCoverageForm.$setDirty();
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
  }]);

