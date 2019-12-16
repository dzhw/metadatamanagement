/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('SimpleMessageToastController',
    function($scope, $mdToast, messages, alert, $state, LanguageService) {
      var language = LanguageService.getCurrentInstantly();
      $scope.messages = messages;
      $scope.alert = alert;
      $scope.go = function(id) {
        $state.go($state.current.name,
          {id: id, lang: language},
          {reload: true, inherit: false, notify: true});
      };
      /* Close Function for Toasts. */
      $scope.closeToast = function() {
        $mdToast.hide();
      };
    });
