/* Author Daniel Katzberg */
'use strict';
angular.module('metadatamanagementApp')
  .controller('DataSetCitateDialogController',
  function($mdDialog, SimpleMessageToastService, citation, $rootScope, $scope) {
    var ctrl = this;
    ctrl.citation = citation;
    $scope.bowser = $rootScope.bowser;

    ctrl.openSuccessCopyToClipboardToast = function(message) {
      SimpleMessageToastService.openSimpleMessageToast(message, []);
    };

    ctrl.closeDialog = function() {
      $mdDialog.hide();
    };
  });
