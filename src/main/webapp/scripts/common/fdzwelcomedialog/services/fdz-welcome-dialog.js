'use strict';

angular.module('metadatamanagementApp').run(function(FdzWelcomeDialogService,
  $timeout) {
    $timeout(function() {
      var openByNavbarFeedbackButton = false;
      FdzWelcomeDialogService.showDialog(openByNavbarFeedbackButton);
    });
  });
