'use strict';

angular.module('metadatamanagementApp')
  .controller('UserConsentController', [
  '$scope',
  '$rootScope',
  '$element',
  'localStorageService',
  'LanguageService',
  '$timeout',
    function($scope, $rootScope, $element, localStorageService,
      LanguageService, $timeout) {

      $scope.$watch(function() {
        return LanguageService.getCurrentInstantly();
      }, function() {
        $scope.lang = LanguageService.getCurrentInstantly();
      });
      if (!localStorageService.get('trackingConsentGiven')) {
        $timeout(function() {
          $element.css('bottom', 0);
        }, 500);
      } else {
        $timeout(function() {
          $element[0].parentNode.removeChild($element[0]);
        }, 500);
      }
      $scope.bowser = $rootScope.bowser;
      $scope.consentGiven = function() {
        localStorageService.set('trackingConsentGiven', true);
        $element.css('bottom', -200);
        $timeout(function() {
          $element[0].parentNode.removeChild($element[0]);
        }, 2000);
      };
      $scope.consentRejected = function() {
        localStorageService.set('trackingConsentGiven', false);
        $element.css('bottom', -200);
        $timeout(function() {
          $element[0].parentNode.removeChild($element[0]);
        }, 2000);
      };
    }]);

