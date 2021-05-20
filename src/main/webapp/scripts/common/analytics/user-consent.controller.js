'use strict';

angular.module('metadatamanagementApp')
  .controller('UserConsentController',
    function($scope, $rootScope, $element, localStorageService,
      LanguageService) {

      $scope.$watch(function() {
        return LanguageService.getCurrentInstantly();
      }, function() {
        $scope.lang = LanguageService.getCurrentInstantly();
      });
      if (!localStorageService.get('trackingConsentGiven')) {
        $element.css('bottom', 0);
      }
      $scope.bowser = $rootScope.bowser;
      $scope.consentGiven = function() {
        localStorageService.set('trackingConsentGiven', true);
        $element.css('bottom', -200);
      };
      $scope.consentRejected = function() {
        localStorageService.set('trackingConsentGiven', false);
        $element.css('bottom', -200);
      };
    }
  );
