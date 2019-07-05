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
      $mdDialog.hide(ctrl.version);
    };
  });
