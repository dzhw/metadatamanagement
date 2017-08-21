/* Author Daniel Katzberg */
'use strict';
angular.module('metadatamanagementApp')
  .controller('DataSetCitateDialogController',
  function($mdDialog, SimpleMessageToastService, citation) {
    var ctrl = this;
    ctrl.citation = citation;

    ctrl.openSuccessCopyToClipboardToast = function(message) {
      SimpleMessageToastService.openSimpleMessageToast(message, []);
    };

    ctrl.closeDialog = function() {
      $mdDialog.hide();
    };
  });
