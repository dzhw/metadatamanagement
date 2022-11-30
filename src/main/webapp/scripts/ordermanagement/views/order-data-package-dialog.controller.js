/* globals _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('OrderDataPackageDialogController', function($scope, $mdDialog,
    $rootScope, MessageBus) {
    $scope.bowser = $rootScope.bowser;
    var ctrl = this;
    ctrl.german = true;
    ctrl.onGoToShoopingCart = MessageBus;

    $scope.$on('currentDataPackage', function(event, value) {
      $scope.dataPackage = value;
    });

    $scope.$on('goToShoppingCartCloseDialog', function() {
      ctrl.cancel();
    });

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
