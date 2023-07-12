'use strict';

angular.module('metadatamanagementApp')
  .controller('CreateOverviewDialogController', function($scope, $mdDialog,
    $rootScope) {
    $scope.bowser = $rootScope.bowser;
    var ctrl = this;
    ctrl.german = true;

    ctrl.cancel = function() {
      $mdDialog.cancel();
    };

    ctrl.validateLanguages = function() {
      if (!ctrl.german && !ctrl.english) {
        $scope.reportForm.german.$setValidity('not-empty', false);
      } else {
        $scope.reportForm.german.$setValidity('not-empty', true);
      }
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
