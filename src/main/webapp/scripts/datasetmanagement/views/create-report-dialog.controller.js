'use strict';

angular.module('metadatamanagementApp')
  .controller('CreateReportDialogController', function($scope, $mdDialog,
    $rootScope) {
    $scope.bowser = $rootScope.bowser;
    var ctrl = this;

    ctrl.cancel = function() {
      $mdDialog.cancel();
    };

    ctrl.ok = function() {
      var languages = [];
      if (ctrl.german) {
        languages.push('de');
      }
      if (ctrl.english) {
        languages.push('en');
      }
      $mdDialog.hide({
        version: ctrl.version,
        languages: languages
      });
    };
  });
