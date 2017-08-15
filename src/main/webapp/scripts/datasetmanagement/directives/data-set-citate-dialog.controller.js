/* Author Daniel Katzberg */
'use strict';
angular.module('metadatamanagementApp')
  .controller('DataSetCitateDialogController',
  function($mdDialog, SimpleMessageToastService) {
    var ctrl = this;
    ctrl.citation = 'Test';

    ctrl.openSuccessCopyToClipboardToast = function(message) {
      SimpleMessageToastService.openSimpleMessageToast(message, []);
    };

    ctrl.closeDialog = function() {
      $mdDialog.hide();
    };
  });
