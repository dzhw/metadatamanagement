'use strict';
angular.module('metadatamanagementApp')
  .controller('DataPackageCitationDialogController',
  function($mdDialog, SimpleMessageToastService, dataPackageCitationHint,
     methodReportsCitationHint, $rootScope, $scope) {
    var ctrl = this;
    ctrl.dataPackageCitationHint = dataPackageCitationHint;
    ctrl.methodReportsCitationHint = methodReportsCitationHint;
    $scope.bowser = $rootScope.bowser;

    ctrl.openSuccessCopyToClipboardToast = function(message) {
      SimpleMessageToastService.openSimpleMessageToast(message, []);
    };

    ctrl.closeDialog = function() {
      $mdDialog.hide();
    };
  });
