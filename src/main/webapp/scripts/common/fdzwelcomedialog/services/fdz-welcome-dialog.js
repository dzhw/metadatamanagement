'use strict';

angular.module('metadatamanagementApp').run(function(FdzWelcomeDialogService,
  $timeout) {
    $timeout(function() {
      FdzWelcomeDialogService.showDialog();
    });
  });
