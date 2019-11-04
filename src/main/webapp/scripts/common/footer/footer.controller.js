'use strict';

var CTRL = function(FdzFeedbackDialogService) {
  this.openDialog = function() {
    var openByNavbarFeedbackButton = true;
    FdzFeedbackDialogService.showDialog(openByNavbarFeedbackButton);
  };
};

angular
  .module('metadatamanagementApp')
  .controller('footerController', CTRL);

